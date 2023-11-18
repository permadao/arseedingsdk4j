package com.github.permadao.arseedingsdk.sdk.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.github.permadao.arseedingsdk.sdk.model.TokenInfo;
import com.github.permadao.arseedingsdk.sdk.request.DataSendRequest;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.arseedingsdk.util.AssertUtils;
import com.github.permadao.model.bundle.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.converter.PayOrderConverter;
import com.github.permadao.arseedingsdk.sdk.model.Pay;
import com.github.permadao.arseedingsdk.sdk.model.PayInfo;
import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.request.ManifestRequest;
import com.github.permadao.arseedingsdk.sdk.request.PayOrdersRequest;
import com.github.permadao.arseedingsdk.sdk.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import com.github.permadao.model.bundle.BundleFee;
import com.github.permadao.model.bundle.BundleOrder;
import com.github.permadao.model.bundle.SubmitBundleDataResponse;
import java.io.InputStream;

import static com.github.permadao.model.constant.UrlPathContant.*;

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
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

  @Override
  public String submitNativeData(
      String apiKey, String currency, byte[] data, String contentType, Map<String, String> tags)
      throws Exception {
    String path = BUNDLE_DATA_PATH + currency;

    HashMap<String, String> headers = buildNativeDataHeaders(apiKey, contentType, tags);

    InputStream responseInputStream = null;
    try {
      responseInputStream = arSeedingService.sendBytesRequestToArSeeding(path, data, headers);
    } catch (Exception e) {
      log.error("send submit native data error", e);
      throw e;
    }

    AssertUtils.notNull(responseInputStream, "submit native data response cannot be null message!");

    SubmitBundleDataResponse submitBundleDataResponse = new SubmitBundleDataResponse();
    try {
      submitBundleDataResponse =
          objectMapper.readValue(responseInputStream, SubmitBundleDataResponse.class);
    } catch (Exception e) {
      log.error("Response trans to SubmitBundleDataResponse failed.", e);
    }
    if (submitBundleDataResponse == null) {
      return null;
    }
    return submitBundleDataResponse.getItemId();
  }

  private HashMap<String, String> buildNativeDataHeaders(
      String apiKey, String contentType, Map<String, String> tags) {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("X-API-KEY", apiKey);
    headers.put("Content-Type", contentType);
    headers.putAll(tags);
    return headers;
  }

  @Override
  public BundleFee bundleFee(long size, String currency) throws Exception {
    String path = BUNDLE_FEE_PATH + size + "/" + currency;
    InputStream responseInputStream = null;
    try {
      responseInputStream = arSeedingService.sendGetRequestToArSeeding(path, new HashMap<>());
    } catch (Exception e) {
      log.error("send bundle fee error", e);
      throw e;
    }

    AssertUtils.notNull(responseInputStream, "bundle fee response cannot be null message!");

    BundleFee bundleFee = new BundleFee();
    try {
      bundleFee = new ObjectMapper().readValue(responseInputStream, BundleFee.class);
    } catch (Exception e) {
      log.error("Response trans to BundleFee failed.", e);
    }
    return bundleFee;
  }

  @Override
  public List<BundleOrder> getOrders(String addr, int startId) throws Exception {
    String path = BUNDLE_ORDERS_PATH + addr;

    HashMap<String, String> headers = buildBundleOrderHeaders(startId);

    InputStream responseInputStream = null;
    try {
      responseInputStream = arSeedingService.sendGetRequestToArSeeding(path, headers);
    } catch (Exception e) {
      log.error("send bundle order error", e);
      throw e;
    }

    AssertUtils.notNull(responseInputStream, "bundle order response cannot be null message!");

    List<BundleOrder> bundleOrderList = new ArrayList<>();
    try {
      bundleOrderList =
              objectMapper.readValue(responseInputStream, new TypeReference<List<BundleOrder>>() {});
    } catch (IOException e) {
      log.error("Response trans to BundleOrder list failed.", e);
    }
    return bundleOrderList;
  }

  private HashMap<String, String> buildBundleOrderHeaders(int startId) {
    HashMap<String, String> headers = new HashMap<>();
    headers.put("cursorId", String.valueOf(startId));
    return headers;
  }
}
