package com.github.permadao.arseedingsdk.sdk.impl;

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
}
