package com.github.permadao.arseedingsdk.sdk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/10/9 00:03
 */
public class TokenInfo implements Serializable {
  private static final long serialVersionUID = 788922298185274580L;
  @JsonProperty("tag")
  private String tag;

  @JsonProperty("id")
  private String id;

  @JsonProperty("symbol")
  private String symbol;

  @JsonProperty("decimals")
  private int decimals;

  @JsonProperty("totalSupply")
  private String totalSupply;

  @JsonProperty("chainType")
  private String chainType;

  @JsonProperty("chainID")
  private String chainID;

  @JsonProperty("burnFees")
  private Map<String, String> burnFees; // key: targetChainType, val: fee

  @JsonProperty("transferFee")
  private String transferFee;

  @JsonProperty("bundleFee")
  private String bundleFee;

  @JsonProperty("holderNum")
  private int holderNum;

  @JsonProperty("crossChainInfoList")
  private Map<String, TargetChain> crossChainInfoList;

  public class TargetChain {
    @JsonProperty("targetChainId")
    private String chainId;

    @JsonProperty("targetChainType")
    private String chainType;

    @JsonProperty("targetDecimals")
    private int decimals;

    @JsonProperty("targetTokenId")
    private String tokenID;

    public String getChainId() {
      return chainId;
    }

    public void setChainId(String chainId) {
      this.chainId = chainId;
    }

    public String getChainType() {
      return chainType;
    }

    public void setChainType(String chainType) {
      this.chainType = chainType;
    }

    public int getDecimals() {
      return decimals;
    }

    public void setDecimals(int decimals) {
      this.decimals = decimals;
    }

    public String getTokenID() {
      return tokenID;
    }

    public void setTokenID(String tokenID) {
      this.tokenID = tokenID;
    }
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getDecimals() {
    return decimals;
  }

  public void setDecimals(int decimals) {
    this.decimals = decimals;
  }

  public String getTotalSupply() {
    return totalSupply;
  }

  public void setTotalSupply(String totalSupply) {
    this.totalSupply = totalSupply;
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

  public Map<String, String> getBurnFees() {
    return burnFees;
  }

  public void setBurnFees(Map<String, String> burnFees) {
    this.burnFees = burnFees;
  }

  public String getTransferFee() {
    return transferFee;
  }

  public void setTransferFee(String transferFee) {
    this.transferFee = transferFee;
  }

  public String getBundleFee() {
    return bundleFee;
  }

  public void setBundleFee(String bundleFee) {
    this.bundleFee = bundleFee;
  }

  public int getHolderNum() {
    return holderNum;
  }

  public void setHolderNum(int holderNum) {
    this.holderNum = holderNum;
  }

  public Map<String, TargetChain> getCrossChainInfoList() {
    return crossChainInfoList;
  }

  public void setCrossChainInfoList(
      Map<String, TargetChain> crossChainInfoList) {
    this.crossChainInfoList = crossChainInfoList;
  }
}
