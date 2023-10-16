package com.github.permadao.arseedingsdk.sdk.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.converter.PayOrderConverter;
import com.github.permadao.arseedingsdk.sdk.model.Pay;
import com.github.permadao.arseedingsdk.sdk.model.PayInfo;
import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.model.TokenInfo;
import com.github.permadao.arseedingsdk.sdk.request.DataSendRequest;
import com.github.permadao.arseedingsdk.sdk.request.ManifestRequest;
import com.github.permadao.arseedingsdk.sdk.request.PayOrdersRequest;
import com.github.permadao.arseedingsdk.sdk.response.*;
import com.github.permadao.model.bundle.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.permadao.model.constant.UrlPathContant.PAY_INFO_PATH;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:54
 */
public class ArHttpSDK implements ArSDK {

  private static final Logger log = LoggerFactory.getLogger(ArHttpSDK.class);

  private ArSeedingService arSeedingService;
  private Wallet wallet;
  private Pay pay;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
    try {
      String payInfoStr = arSeedingService.sendPayRequest(PAY_INFO_PATH);
      PayInfo payInfo = objectMapper.readValue(payInfoStr, PayInfo.class);
      Map<String, TokenInfo> tokens = new HashMap<>();
      for (TokenInfo t : payInfo.getTokenList()) {
        tokens.put(t.getTag(), t);
      }
      pay = new Pay(arSeedingService, tokens, payInfo.getFeeRecipient());
    } catch (Exception e) {
      log.error("get pay info error", e);
    }
  }

  public static ArSDK buildArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
    return new ArHttpSDK(arSeedingService, wallet);
  }

  @Override
  public DataSendOrderResponse sendDataAndPay(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence)
      throws Exception {

    DataSendResponse dataSendResponse =
        new DataSendRequest(arSeedingService, wallet)
            .send(data, currency, tags, target, anchor, needSequence);

    List<PayOrder> payOrderList = new ArrayList<>();
    payOrderList.add(PayOrderConverter.dataSendResponseConvertToPayOrder(dataSendResponse));

    PayOrdersResponse payOrdersResponse =
        new PayOrdersRequest(arSeedingService, wallet, pay).send(payOrderList);

    return PayOrderConverter.payOrdersResponseConvertToDataSendOrderResponse(payOrdersResponse);
  }

  @Override
  public DataSendResponse sendData(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence)
      throws Exception {

    return new DataSendRequest(arSeedingService, wallet)
        .send(data, currency, tags, target, anchor, needSequence);
  }

  @Override
  public PayOrdersResponse payOrders(List<PayOrder> orders) throws Exception {

    return new PayOrdersRequest(arSeedingService, wallet, pay).send(orders);
  }

  @Override
  public ManifestUploadResponse uploadFolder(
      String rootPath, int batchSize, String indexFile, String currency) throws Exception {
    return new ManifestRequest(this).uploadFolder(rootPath, batchSize, indexFile, currency);
  }

  @Override
  public ManifestUploadResponse uploadFolderWithNoFee(
      String rootPath, int batchSize, String indexFile) throws Exception {
    return new ManifestRequest(this).uploadFolderWithNoFee(rootPath, batchSize, indexFile);
  }

  @Override
  public ManifestUploadResponse uploadFolderWithSequence(
      String rootPath, int batchSize, String indexFile) throws Exception {
    return new ManifestRequest(this).uploadFolderWithSequence(rootPath, batchSize, indexFile);
  }

  @Override
  public UploadFolderAndPayResponse uploadFolderAndPay(
      String rootPath, int batchSize, String indexFile, String currency) throws Exception {
    PayOrdersRequest payOrdersRequest = new PayOrdersRequest(arSeedingService, wallet, pay);
    return new ManifestRequest(this)
        .uploadFolderAndPay(rootPath, batchSize, indexFile, currency, payOrdersRequest);
  }

  @Override
  public ManifestUploadResponse uploadFolder(
      String rootPath, int batchSize, String indexFile, String currency, boolean needSequence)
      throws Exception {
    return new ManifestRequest(this)
        .uploadFolder(rootPath, batchSize, indexFile, currency, needSequence);
  }
}
