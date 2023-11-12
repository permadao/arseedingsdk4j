package com.github.permadao.arseedingsdk.sdk.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/9 00:50
 */
public class AccountBalances implements Serializable {
  private static final long serialVersionUID = -5978475138972172438L;

  private String accid;

  private List<Balance> balances;

  public static class Balance {

    private String tag;

    private String amount;

    private int decimals;

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public String getAmount() {
      return amount;
    }

    public void setAmount(String amount) {
      this.amount = amount;
    }

    public int getDecimals() {
      return decimals;
    }

    public void setDecimals(int decimals) {
      this.decimals = decimals;
    }
  }

  public String getAccid() {
    return accid;
  }

  public void setAccid(String accid) {
    this.accid = accid;
  }

  public List<Balance> getBalances() {
    return balances;
  }

  public void setBalances(List<Balance> balances) {
    this.balances = balances;
  }
}
