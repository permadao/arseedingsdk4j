package com.github.permadao.arseedingsdk.sdk.impl;
import com.github.permadao.arseedingsdk.sdk.model.TokenInfo;
import com.github.permadao.arseedingsdk.sdk.request.DataSendRequest;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.model.bundle.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.model.PayInfo;
import com.github.permadao.arseedingsdk.sdk.model.TokenInfo;
import com.github.permadao.arseedingsdk.sdk.request.DataSendRequest;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.model.bundle.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.permadao.model.bundle.BundleFee;
import com.github.permadao.model.bundle.BundleItem;
import com.github.permadao.model.bundle.BundleOrder;
import com.github.permadao.model.bundle.SubmitBundleDataResponse;
import okhttp3.internal.http2.Header;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:54
 */
public class ArHttpSDK implements ArSDK {
  private ArSeedingService arSeedingService;
  private Wallet wallet;
  private Map<String, TokenInfo> tokens;
  private String feeRecipient;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
    try {
      String payInfoStr = arSeedingService.sendPayRequest("/info");
      PayInfo payInfo = objectMapper.readValue(payInfoStr, PayInfo.class);
      Map<String, TokenInfo> tokens = new HashMap<>();
      for (TokenInfo t : payInfo.getTokenList()) {
        tokens.put(t.getTag(), t);
      }
      this.tokens = tokens;
      this.feeRecipient = payInfo.getFeeRecipient();
    } catch (Exception e) {

    }
  }

  public static ArSDK buildArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
      return new ArHttpSDK(arSeedingService, wallet);
  }

  @Override
  public DataSendOrderResponse sendDataAndPay(byte[] data, String currency, List<Tag> tags,
      String target, String anchor, boolean needSequence) throws Exception {
    return new DataSendRequest(arSeedingService, wallet).send(data, currency, tags,
            target, anchor, needSequence);

  }


    public String SubmitNativeData(String apiKey, String currency, byte[] data, String contentType, Map<String, String> tags) {
        String path = "/bundle/data/" + currency;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-API-KEY", apiKey);
        headers.put("Content-Type", contentType);
        headers.putAll(tags);
        InputStream responseInputStream = null;
        try {
            responseInputStream  = arSeedingService.sendBytesRequestToArSeeding(path, data, headers);
        } catch (IOException e) {
            // TODO change to print log
            System.out.println(e.getMessage());
        }
        if(Objects.isNull(responseInputStream)){
            System.out.println("Cannot get response message!");
        }
        SubmitBundleDataResponse submitBundleDataResponse = new SubmitBundleDataResponse();
        try{
            submitBundleDataResponse = new ObjectMapper().readValue(responseInputStream, SubmitBundleDataResponse.class);
        }catch (Exception e){
            System.out.println("Response trans to SubmitBundleDataResponse failed.");
        }
        return submitBundleDataResponse.getItemId();
    }

    public BundleFee BundleFee(long size , String currency ){
        String path = "/bundle/fee/" + size + "/" + currency;
        InputStream responseInputStream = null;
        try {
            responseInputStream  = arSeedingService.sendGetRequestToArSeeding(path, new HashMap<>());
        } catch (IOException e) {
            // TODO change to print log
            System.out.println(e.getMessage());
        }
        if(Objects.isNull(responseInputStream)){
            System.out.println("Cannot get response message!");
        }
        BundleFee bundleFee = new BundleFee();
        try{
            bundleFee = new ObjectMapper().readValue(responseInputStream, BundleFee.class);
        }catch (Exception e){
            System.out.println("Response trans to BundleFee failed.");
        }
        return bundleFee;
    }

    public List<BundleOrder> GetOrders(String addr, int startId){
        String path = "/bundle/orders/" + addr;
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cursorId", String.valueOf(startId));
        InputStream responseInputStream = null;
        try {
            responseInputStream  = arSeedingService.sendGetRequestToArSeeding(path, headers);
        } catch (IOException e) {
            // TODO change to print log
            System.out.println(e.getMessage());
        }
        if(Objects.isNull(responseInputStream)){
            System.out.println("Cannot get response message!");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<BundleOrder> bundleOrderList = new ArrayList<>();
        try {
            bundleOrderList = objectMapper.readValue(responseInputStream, new TypeReference<List<BundleOrder>>(){});
        } catch (IOException e) {
            System.out.println("Response trans to BundleOrder list failed.");
        }
        return bundleOrderList;
    }

}
