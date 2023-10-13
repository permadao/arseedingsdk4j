package com.github.permadao.model.bundle;


public class BundleOrder {
    private int id;
    // TODO 从JSON转换为Object时存在时间转换问题，待定是否引入序列化的包
    private String createdAt;
    private String updatedAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
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
}
