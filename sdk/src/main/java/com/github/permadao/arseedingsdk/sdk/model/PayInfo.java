package com.github.permadao.arseedingsdk.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/10/9 00:01
 */
public class PayInfo implements Serializable {
  private static final long serialVersionUID = 2198815382494069946L;
  @JsonProperty("isSynced")
  private boolean isSynced;

  @JsonProperty("isClosed")
  private boolean isClosed;

  @JsonProperty("balanceRootHash")
  private String balanceRootHash;

  @JsonProperty("rootHash")
  private String rootHash;

  @JsonProperty("everRootHash")
  private String everRootHash;

  @JsonProperty("owner")
  private String owner;

  @JsonProperty("setActionOwner")
  private String setActionOwner;

  @JsonProperty("ethChainID")
  private String ethChainID;

  @JsonProperty("feeRecipient")
  private String feeRecipient;

  @JsonProperty("ethLocker")
  private String ethLocker;

  @JsonProperty("arLocker")
  private String arLocker;

  @JsonProperty("lockers")
  private Map<String, String> lockers;

  @JsonProperty("tokenList")
  private List<TokenInfo> tokenList;

  public boolean isSynced() {
    return isSynced;
  }

  public void setSynced(boolean synced) {
    isSynced = synced;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public void setClosed(boolean closed) {
    isClosed = closed;
  }

  public String getBalanceRootHash() {
    return balanceRootHash;
  }

  public void setBalanceRootHash(String balanceRootHash) {
    this.balanceRootHash = balanceRootHash;
  }

  public String getRootHash() {
    return rootHash;
  }

  public void setRootHash(String rootHash) {
    this.rootHash = rootHash;
  }

  public String getEverRootHash() {
    return everRootHash;
  }

  public void setEverRootHash(String everRootHash) {
    this.everRootHash = everRootHash;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSetActionOwner() {
    return setActionOwner;
  }

  public void setSetActionOwner(String setActionOwner) {
    this.setActionOwner = setActionOwner;
  }

  public String getEthChainID() {
    return ethChainID;
  }

  public void setEthChainID(String ethChainID) {
    this.ethChainID = ethChainID;
  }

  public String getFeeRecipient() {
    return feeRecipient;
  }

  public void setFeeRecipient(String feeRecipient) {
    this.feeRecipient = feeRecipient;
  }

  public String getEthLocker() {
    return ethLocker;
  }

  public void setEthLocker(String ethLocker) {
    this.ethLocker = ethLocker;
  }

  public String getArLocker() {
    return arLocker;
  }

  public void setArLocker(String arLocker) {
    this.arLocker = arLocker;
  }

  public Map<String, String> getLockers() {
    return lockers;
  }

  public void setLockers(Map<String, String> lockers) {
    this.lockers = lockers;
  }

  public List<TokenInfo> getTokenList() {
    return tokenList;
  }

  public void setTokenList(List<TokenInfo> tokenList) {
    this.tokenList = tokenList;
  }
}
