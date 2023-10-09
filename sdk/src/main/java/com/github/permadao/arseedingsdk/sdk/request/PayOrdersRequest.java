package com.github.permadao.arseedingsdk.sdk.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.model.AccountBalances;
import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.model.PayTransaction;
import com.github.permadao.arseedingsdk.sdk.model.TokenInfo;
import com.github.permadao.arseedingsdk.sdk.response.PayOrdersResponse;
import com.github.permadao.arseedingsdk.util.AssertUtils;
import com.github.permadao.arseedingsdk.util.EverPayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/10/8 17:02
 */
public class PayOrdersRequest {

  private ArSeedingService arSeedingService;

  private Wallet wallet;

  private Map<String, TokenInfo> tokens;
  private String feeRecipient;

  protected final ObjectMapper objectMapper = new ObjectMapper();

  public PayOrdersRequest(
      ArSeedingService arSeedingService,
      Wallet wallet,
      Map<String, TokenInfo> tokens,
      String feeRecipient) {
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
    this.tokens = tokens;
    this.feeRecipient = feeRecipient;
  }

  public PayOrdersResponse send(List<PayOrder> orders) throws Exception {

    verifyOrders(orders);

    if (orders.get(0).getFee().isEmpty()) {
      return null; // arseeding NO_FEE module
    }

    BigDecimal totalFee = BigDecimal.ZERO;
    List<String> itemIds = new ArrayList<>();
    for (PayOrder ord : orders) {
      try {
        BigDecimal fee = new BigDecimal(ord.getFee());
        totalFee = totalFee.add(fee);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("order fee incorrect");
      }
      itemIds.add(ord.getItemId());
    }

    Map<String, Object> payTxData = new HashMap<>();
    payTxData.put("appName", "arseeding");
    payTxData.put("action", "payment");
    payTxData.put("itemIds", itemIds);

    String dataJs = objectMapper.writeValueAsString(payTxData);

    List<String> tokenTags = symbolToTagArr(orders.get(0).getCurrency());
    if (tokenTags.isEmpty()) {
      throw new IllegalArgumentException("currency not exist token");
    }

    List<AccountBalances.Balance> tokBals = getAccountBalances(wallet.getAddress()).getBalances();
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
    if (useTag.isEmpty()) {
      throw new IllegalArgumentException("token balance insufficient");
    }

    TokenInfo tokenInfo = tokens.get(useTag);
    AssertUtils.notNull(tokenInfo, "TokenInfo is null, useTag = " + useTag);
    String action = "transfer";
    PayTransaction payTransaction =
        buildPayTransaction(tokenInfo, action, totalFee, dataJs, orders.get(0).getBundler());

    // sign tx

    String jsonStr = objectMapper.writeValueAsString(payTransaction);
    InputStream inputStream = arSeedingService.sendJsonRequestToArSeeding("/tx", jsonStr, null);

    return objectMapper.readValue(inputStream, PayOrdersResponse.class);
  }

  private PayTransaction buildPayTransaction(
      TokenInfo tokenInfo, String action, BigDecimal totalFee, String dataJs, String bundler) {
    PayTransaction tx = new PayTransaction();
    tx.setAction(action);
    tx.setAmount(totalFee == null ? "0" : totalFee.toString());
    tx.setChainID(tokenInfo.getChainID());
    tx.setData(dataJs);
    tx.setFee(tokenInfo.getTransferFee());
    tx.setFrom(wallet.getAddress());
    tx.setChainType(tokenInfo.getChainType());
    tx.setNonce(String.valueOf(EverPayUtils.getNonce()));
    tx.setTo(bundler);
    tx.setFeeRecipient(feeRecipient);
    tx.setTokenID(tokenInfo.getId());
    tx.setTokenSymbol(tokenInfo.getSymbol());
    tx.setVersion("v1");
    return tx;
  }

  private AccountBalances getAccountBalances(String address) throws IOException {
    String pathName = String.format("/balances/%s", address);
    String balancesStr = arSeedingService.sendPayRequest(pathName);
    return objectMapper.readValue(balancesStr, AccountBalances.class);
  }

  public List<String> symbolToTagArr(String symbol) {
    List<String> tagArr = new ArrayList<>();

    for (Map.Entry<String, TokenInfo> entry : tokens.entrySet()) {
      TokenInfo tok = entry.getValue();
      if (tok.getSymbol().equalsIgnoreCase(symbol)) {
        tagArr.add(entry.getKey());
      }
    }

    return tagArr;
  }

  private void verifyOrders(List<PayOrder> orders) {
    if (orders.isEmpty()) {
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
