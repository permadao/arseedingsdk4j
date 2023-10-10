package com.github.permadao.arseedingsdk.sdk.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * @author shiwen.wy
 * @date 2023/10/10 00:04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTransaction implements Serializable {
  private static final long serialVersionUID = 4106036955242154664L;

  @JsonProperty("tokenSymbol")
  private String tokenSymbol;

  @JsonProperty("action")
  private String action;

  @JsonProperty("from")
  private String from;

  @JsonProperty("to")
  private String to;

  @JsonProperty("amount")
  private String amount;

  @JsonProperty("fee")
  private String fee;

  @JsonProperty("feeRecipient")
  private String feeRecipient;

  @JsonProperty("nonce")
  private String nonce;

  @JsonProperty("tokenID")
  private String tokenID;

  @JsonProperty("chainType")
  private String chainType;

  @JsonProperty("chainID")
  private String chainID;

  @JsonProperty("data")
  private String data;

  @JsonProperty("version")
  private String version;

  @JsonProperty("sig")
  private String sig;

  public String getTokenSymbol() {
    return tokenSymbol;
  }

  public void setTokenSymbol(String tokenSymbol) {
    this.tokenSymbol = tokenSymbol;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getFee() {
    return fee;
  }

  public void setFee(String fee) {
    this.fee = fee;
  }

  public String getFeeRecipient() {
    return feeRecipient;
  }

  public void setFeeRecipient(String feeRecipient) {
    this.feeRecipient = feeRecipient;
  }

  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public String getTokenID() {
    return tokenID;
  }

  public void setTokenID(String tokenID) {
    this.tokenID = tokenID;
  }

  public String getChainType() {
    return chainType;
  }

  public void setChainType(String chainType) {
    this.chainType = chainType;
  }

  public String getChainID() {
    return chainID;
  }

  public void setChainID(String chainID) {
    this.chainID = chainID;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getSig() {
    return sig;
  }

  public void setSig(String sig) {
    this.sig = sig;
  }

  public String string() {
    return "tokenSymbol: " + tokenSymbol + "\n" +
        "action: " + action + "\n" +
        "from: " + from + "\n" +
        "to: " + to + "\n" +
        "amount: " + amount + "\n" +
        "fee: " + fee + "\n" +
        "feeRecipient: " + feeRecipient + "\n" +
        "nonce: " + nonce + "\n" +
        "tokenID: " + tokenID + "\n" +
        "chainType: " + chainType + "\n" +
        "chainID: " + chainID + "\n" +
        "data: " + data + "\n" +
        "version: " + version;
  }
}
