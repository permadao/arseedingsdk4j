package com.github.permadao.arseedingsdk.sdk.response;

import com.github.permadao.arseedingsdk.sdk.model.PayOrder;

import java.io.Serializable;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/16 21:57
 */
public class ManifestUploadResponse implements Serializable {
  private static final long serialVersionUID = 5636810231887069345L;
  private List<PayOrder> orders;
  private String itemId;

  public ManifestUploadResponse(List<PayOrder> orders, String itemId) {
    this.orders = orders;
    this.itemId = itemId;
  }

  public List<PayOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<PayOrder> orders) {
    this.orders = orders;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  @Override
  public String toString() {
    return "ManifestUploadResponse{" +
            "orders=" + orders +
            ", itemId='" + itemId + '\'' +
            '}';
  }
}
