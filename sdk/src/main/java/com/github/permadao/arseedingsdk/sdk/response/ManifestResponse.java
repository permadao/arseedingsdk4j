package com.github.permadao.arseedingsdk.sdk.response;

import com.github.permadao.model.scheam.RespOrder;
import com.github.permadao.model.tx.Transaction;

import java.util.List;

public class ManifestResponse {


    public static class UploadResponse{
          private List<RespOrder> orders;
          private String itemId;

          public UploadResponse(List<RespOrder> orders, String itemId) {
              this.orders = orders;
              this.itemId = itemId;
          }

          public List<RespOrder> getOrders() {
              return orders;
          }

          public void setOrders(List<RespOrder> orders) {
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
        private List<Transaction> everTxs;
        public UploadFolderAndPayResponse(List<RespOrder> orders, String itemId, List<Transaction> everTxs) {
            super();
        }

        public UploadResponse getUploadResponse() {
            return uploadResponse;
        }

        public void setUploadResponse(UploadResponse uploadResponse) {
            this.uploadResponse = uploadResponse;
        }

        public List<Transaction> getEverTxs() {
            return everTxs;
        }

        public void setEverTxs(List<Transaction> everTxs) {
            this.everTxs = everTxs;
        }
    }
}
