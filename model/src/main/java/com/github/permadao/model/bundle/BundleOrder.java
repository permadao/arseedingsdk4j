package com.github.permadao.model.bundle;

import java.util.Date;

public class BundleOrder {
    private int id;
    private Date createdAt;
    private Date updatedAt;
    private String itemId;
    private String signer;
    private int signType;
    private long size;
    private String currency;
    private int decimals;
    private String fee;
    private long paymentExpiredTime;
    private long expectedBlock;
    private String paymentStatus;
    private String paymentId;
    private String onChainStatus;
    private String apiKey;
    private boolean sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public int getSignType() {
        return signType;
    }

    public void setSignType(int signType) {
        this.signType = signType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public long getPaymentExpiredTime() {
        return paymentExpiredTime;
    }

    public void setPaymentExpiredTime(long paymentExpiredTime) {
        this.paymentExpiredTime = paymentExpiredTime;
    }

    public long getExpectedBlock() {
        return expectedBlock;
    }

    public void setExpectedBlock(long expectedBlock) {
        this.expectedBlock = expectedBlock;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOnChainStatus() {
        return onChainStatus;
    }

    public void setOnChainStatus(String onChainStatus) {
        this.onChainStatus = onChainStatus;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    @Override public String toString() {
        return "BundleOrder{" + "id=" + id + ", createdAt='" + createdAt + '\''
                       + ", updatedAt='" + updatedAt + '\'' + ", itemId='"
                       + itemId + '\'' + ", signer='" + signer + '\''
                       + ", signType=" + signType + ", size=" + size
                       + ", currency='" + currency + '\'' + ", decimals="
                       + decimals + ", fee='" + fee + '\''
                       + ", paymentExpiredTime=" + paymentExpiredTime
                       + ", expectedBlock=" + expectedBlock
                       + ", paymentStatus='" + paymentStatus + '\''
                       + ", paymentId='" + paymentId + '\''
                       + ", onChainStatus='" + onChainStatus + '\''
                       + ", apiKey='" + apiKey + '\'' + ", sort=" + sort + '}';
    }
}
