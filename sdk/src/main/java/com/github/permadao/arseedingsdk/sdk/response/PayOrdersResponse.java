package com.github.permadao.arseedingsdk.sdk.response;

import java.io.Serializable;

/**
 * @author shiwen.wy
 * @date 2023/10/8 22:23
 */
public class PayOrdersResponse implements Serializable {
  private static final long serialVersionUID = 8107322905600585006L;

  private String status;

  private String tokenSymbol;

  private String action;

  private String from;

  private String to;

  private String amount;

  private String fee;

  private String feeRecipient;

  private String nonce;

  private String tokenID;

  private String chainType;

  private String chainID;

  private String data;

  private String version;

  private String sig;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

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
}
