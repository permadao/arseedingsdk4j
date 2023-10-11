package com.github.permadao.arseedingsdk.sdk.response;

import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.model.PayTransaction;

import java.util.List;

public class ManifestResponse {


    public static class UploadResponse{
          private List<PayOrder> orders;
          private String itemId;

          public UploadResponse(List<PayOrder> orders, String itemId) {
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
      }

    public static class UploadFolderAndPayResponse{
        private UploadResponse uploadResponse;
        private List<PayTransaction> everTxs;

        public UploadFolderAndPayResponse(UploadResponse uploadResponse, List<PayTransaction> everTxs) {
            this.uploadResponse = uploadResponse;
            this.everTxs = everTxs;
        }

        public UploadResponse getUploadResponse() {
            return uploadResponse;
        }

        public void setUploadResponse(UploadResponse uploadResponse) {
            this.uploadResponse = uploadResponse;
        }

        public List<PayTransaction> getEverTxs() {
            return everTxs;
        }

        public void setEverTxs(List<PayTransaction> everTxs) {
            this.everTxs = everTxs;
        }
    }
}
