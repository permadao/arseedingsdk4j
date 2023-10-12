package com.github.permadao.arseedingsdk.sdk.impl;

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
    public static ArSDK buildArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
      return new com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK(arSeedingService, wallet);
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

    public static void main(String[] args) {
//        String s = "{\n" +
//                "    \"signatureType\": 3,\n" +
//                "    \"signature\": \"DC469T6Fz3ByFCtEjnP9AdsLSDFqINxvbIqFw1qwk0ApHtpmytRWFHZeY2gBN9nXopzY7Sbi9u5U6UcpPrwPlxs\",\n" +
//                "    \"owner\": \"BCLR8qIeP8-kDAO6AifvSSzyCQJBwAtPYErCaX1LegK7GwXmyMvhzCmt1x6vLw4xixiOrI34ObhU2e1RGW5YNXo\",\n" +
//                "    \"target\": \"\",\n" +
//                "    \"anchor\": \"\",\n" +
//                "    \"tags\": [\n" +
//                "        {\n" +
//                "          \"name\": \"a\",\n" +
//                "          \"value\": \"aa\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"name\": \"b\",\n" +
//                "          \"value\": \"bb\"\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"name\": \"Content-Type\",\n" +
//                "          \"value\": \"image/png\"\n" +
//                "        }\n" +
//                "      ],\n" +
//                "    \"data\": \"\",\n" +
//                "    \"id\": \"IlYC5sG61mhTOlG2Ued5LWxN4nuhyZh3ror0MBbPKy4\"\n" +
//                "}\n";
//        ObjectMapper objectMapper = new ObjectMapper();
//        BundleItem bundleItem = new BundleItem();
//        try{
//            bundleItem = objectMapper.readValue(new ByteArrayInputStream(s.getBytes()), BundleItem.class);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println(bundleItem.getSignatureType());

        String s = "[\n" +
                "    {\n" +
                "        \"id\": 13,\n" +
                "        \"createdAt\": \"2022-07-11T04:07:12.261Z\",\n" +
                "        \"updatedAt\": \"2022-07-11T05:07:44.369Z\",\n" +
                "        \"itemId\": \"n6Xv8LwdpsQgpaTQgaXQfUORW-KxYePDnj-1ka9dHxM\",\n" +
                "        \"signer\": \"0x4002ED1a1410aF1b4930cF6c479ae373dEbD6223\",\n" +
                "        \"signType\": 3,\n" +
                "        \"size\": 7802,\n" +
                "        \"currency\": \"USDT\",\n" +
                "        \"decimals\": 6,\n" +
                "        \"fee\": \"817\",\n" +
                "        \"paymentExpiredTime\": 1657516032,\n" +
                "        \"expectedBlock\": 972166,\n" +
                "        \"paymentStatus\": \"expired\",\n" +
                "        \"paymentId\": \"\",\n" +
                "        \"onChainStatus\": \"failed\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": 14,\n" +
                "        \"createdAt\": \"2022-07-11T04:07:12.261Z\",\n" +
                "        \"updatedAt\": \"2022-07-11T05:07:44.369Z\",\n" +
                "        \"itemId\": \"n6Xv8LwdpsQgpaTQgaXQfUORW-KxYePDnj-1ka9dHxM\",\n" +
                "        \"signer\": \"0x4002ED1a1410aF1b4930cF6c479ae373dEbD6223\",\n" +
                "        \"signType\": 3,\n" +
                "        \"size\": 7802,\n" +
                "        \"currency\": \"USDT\",\n" +
                "        \"decimals\": 6,\n" +
                "        \"fee\": \"817\",\n" +
                "        \"paymentExpiredTime\": 1657516032,\n" +
                "        \"expectedBlock\": 972166,\n" +
                "        \"paymentStatus\": \"expired\",\n" +
                "        \"paymentId\": \"\",\n" +
                "        \"onChainStatus\": \"failed\"\n" +
                "    }\n" +
                "]\n";
//        System.out.println("now = " + LocalTime.now());
        ObjectMapper objectMapper = new ObjectMapper();
        List<BundleOrder> cars1 = new ArrayList<>();
        try {
           cars1 = objectMapper.readValue(s, new TypeReference<List<BundleOrder>>(){});
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        cars1.stream().forEach(ss -> {
            System.out.println(ss.getId());
            System.out.println(ss.getFee());
            System.out.println(ss.getCreatedAt());
        });
    }
}
