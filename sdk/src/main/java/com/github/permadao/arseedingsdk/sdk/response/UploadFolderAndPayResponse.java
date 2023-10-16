package com.github.permadao.arseedingsdk.sdk.response;

import com.github.permadao.arseedingsdk.sdk.model.PayTransaction;

import java.io.Serializable;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/16 23:35
 */
public class UploadFolderAndPayResponse implements Serializable {
  private static final long serialVersionUID = -4910163125898862318L;
  private ManifestUploadResponse manifestUploadResponse;
  private List<PayTransaction> everTxs;

  public UploadFolderAndPayResponse(
      ManifestUploadResponse manifestUploadResponse, List<PayTransaction> everTxs) {
    this.manifestUploadResponse = manifestUploadResponse;
    this.everTxs = everTxs;
  }

  public ManifestUploadResponse getManifestUploadResponse() {
    return manifestUploadResponse;
  }

  public void setManifestUploadResponse(ManifestUploadResponse manifestUploadResponse) {
    this.manifestUploadResponse = manifestUploadResponse;
  }

  public List<PayTransaction> getEverTxs() {
    return everTxs;
  }

  public void setEverTxs(List<PayTransaction> everTxs) {
    this.everTxs = everTxs;
  }
}
