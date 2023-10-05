package com.github.permadao.model.bundle;

public class BundleFee {
    private String currency;
    private int decimals;
    private String finalFee;

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

    public String getFinalFee() {
        return finalFee;
    }

    public void setFinalFee(String finalFee) {
        this.finalFee = finalFee;
    }
}
