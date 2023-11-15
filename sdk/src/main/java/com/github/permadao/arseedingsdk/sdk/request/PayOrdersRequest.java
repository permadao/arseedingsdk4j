package com.github.permadao.arseedingsdk.sdk.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.model.*;
import com.github.permadao.arseedingsdk.sdk.response.PayOrdersResponse;
import com.github.permadao.arseedingsdk.util.AssertUtils;
import com.github.permadao.arseedingsdk.util.EverPayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.permadao.model.constant.PayContant.*;
import static com.github.permadao.model.constant.UrlPathContant.PAY_TX_PATH;
import static com.github.permadao.model.constant.UrlPathContant.QUERY_BALANCES;

/**
 * @author shiwen.wy
 * @date 2023/10/8 17:02
 */
public class PayOrdersRequest {

  private static final Logger log = LoggerFactory.getLogger(PayOrdersRequest.class);

  private final ArSeedingService arSeedingService;

  private final Wallet wallet;

  private final Pay pay;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public PayOrdersRequest(
      ArSeedingService arSeedingService,
      Wallet wallet, Pay pay) {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
    this.pay = pay;
  }

  public PayOrdersResponse send(List<PayOrder> orders) throws Exception {

    verifyOrders(orders);

    if (StringUtils.isBlank(orders.get(0).getFee())) {
      log.info("order fee is null");
      return null;
    }

    BigDecimal totalFee = BigDecimal.ZERO;
    List<String> itemIds = new ArrayList<>();
    for (PayOrder order : orders) {
      try {
        totalFee = totalFee.add(new BigDecimal(order.getFee()));
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("order fee incorrect");
      }
      itemIds.add(order.getItemId());
    }

    String dataJs = buildPayTxStr(itemIds);

    List<String> tokenTags = pay.symbolToTagArr(orders.get(0).getCurrency());
    if (tokenTags.isEmpty()) {
      throw new IllegalArgumentException("currency not exist token");
    }

    List<AccountBalances.Balance> tokBals = pay.getAccountBalances(wallet.getAddress()).getBalances();
    Map<String, BigDecimal> tagToBal = new HashMap<>();
    for (AccountBalances.Balance bal : tokBals) {
      try {
        BigDecimal amt = new BigDecimal(bal.getAmount());
        tagToBal.put(bal.getTag(), amt);
      } catch (NumberFormatException e) {
        // Handle the parsing error as needed
      }
    }

    String useTag = "";
    for (String tag : tokenTags) {
      BigDecimal amt = tagToBal.get(tag);
      if (amt != null && amt.compareTo(totalFee) >= 0) {
        useTag = tag;
      }
    }
    AssertUtils.notBlank(useTag, "token balance insufficient");

    TokenInfo tokenInfo = pay.getTokens().get(useTag);
    AssertUtils.notNull(tokenInfo, "TokenInfo is null, useTag = " + useTag);

    PayTransaction payTransaction =
        buildPayTransaction(tokenInfo, totalFee, dataJs, orders.get(0).getBundler());

    payTransaction.setSig(
        wallet.payTxSign(payTransaction.string().getBytes(StandardCharsets.UTF_8)));

    String jsonStr = objectMapper.writeValueAsString(payTransaction);

    InputStream inputStream =
        arSeedingService.sendJsonRequestToEverPay(PAY_TX_PATH, jsonStr, null);

    return objectMapper.readValue(inputStream, PayOrdersResponse.class);
  }

  private String buildPayTxStr(List<String> itemIds) throws JsonProcessingException {
    Map<String, Object> payTxData = new HashMap<>();
    payTxData.put(APP_NAME_CODE, APP_NAME_VALUE);
    payTxData.put(ACTION_CODE, ACTION_VALUE);
    payTxData.put(PAY_ITEM_ID_LIST, itemIds);

    return objectMapper.writeValueAsString(payTxData);
  }

  private PayTransaction buildPayTransaction(
      TokenInfo tokenInfo, BigDecimal totalFee, String dataJs, String bundler) {
    PayTransaction tx = new PayTransaction();
    tx.setAction(PAY_ACTION);
    tx.setAmount(totalFee == null ? "0" : totalFee.toString());
    tx.setChainID(tokenInfo.getChainID());
    tx.setData(dataJs);
    tx.setFee(tokenInfo.getTransferFee());
    tx.setFrom(wallet.getAddress());
    tx.setChainType(tokenInfo.getChainType());
    tx.setNonce(String.valueOf(EverPayUtils.getNonce()));
    tx.setTo(bundler);
    tx.setFeeRecipient(pay.getFeeRecipient());
    tx.setTokenID(tokenInfo.getId());
    tx.setTokenSymbol(tokenInfo.getSymbol());
    tx.setVersion(PAY_VERSION);
    return tx;
  }

  private void verifyOrders(List<PayOrder> orders) {
    if (orders == null || orders.isEmpty()) {
      throw new IllegalArgumentException("order is null");
    }

    // orders can not be more than 500
    if (orders.size() > 500) {
      throw new IllegalArgumentException("please use BatchPayOrders function");
    }

    // Check orders
    if (orders.size() > 1) {
      String bundler = orders.get(0).getBundler();
      String currency = orders.get(0).getCurrency();
      for (PayOrder ord : orders.subList(1, orders.size())) {
        if (!ord.getBundler().equals(bundler) || !ord.getCurrency().equals(currency)) {
          throw new IllegalArgumentException("orders bundler and currency must be equal");
        }
      }
    }
  }
}
