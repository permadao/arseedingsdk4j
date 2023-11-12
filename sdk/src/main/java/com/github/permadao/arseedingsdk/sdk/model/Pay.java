package com.github.permadao.arseedingsdk.sdk.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ArSeedingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.permadao.model.constant.UrlPathContant.QUERY_BALANCES;

/**
 * @author shiwen.wy
 * @date 2023/10/16 15:13
 */
public class Pay {
  private ArSeedingService arSeedingService;
  private Map<String, TokenInfo> tokens;
  private String feeRecipient;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public Pay(
      ArSeedingService arSeedingService, Map<String, TokenInfo> tokens, String feeRecipient) {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.tokens = tokens;
    this.feeRecipient = feeRecipient;
    this.arSeedingService = arSeedingService;
  }

  public AccountBalances getAccountBalances(String address) throws IOException {
    String pathName = String.format(QUERY_BALANCES, address);
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

  public Map<String, TokenInfo> getTokens() {
    return tokens;
  }

  public String getFeeRecipient() {
    return feeRecipient;
  }
}
