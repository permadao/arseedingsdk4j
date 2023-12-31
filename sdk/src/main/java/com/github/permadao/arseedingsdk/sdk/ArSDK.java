package com.github.permadao.arseedingsdk.sdk;

import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.response.*;
import com.github.permadao.model.bundle.BundleFee;
import com.github.permadao.model.bundle.BundleOrder;
import com.github.permadao.model.bundle.Tag;

import java.util.List;
import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:53
 */
public interface ArSDK {

  DataSendOrderResponse sendDataAndPay(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence)
      throws Exception;

  DataSendResponse sendData(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence)
      throws Exception;

  PayOrdersResponse payOrders(List<PayOrder> orders) throws Exception;

  ManifestUploadResponse uploadFolder(
      String rootPath, int batchSize, String indexFile, String currency) throws Exception;

  ManifestUploadResponse uploadFolderWithNoFee(String rootPath, int batchSize, String indexFile)
      throws Exception;

  ManifestUploadResponse uploadFolderWithSequence(String rootPath, int batchSize, String indexFile)
      throws Exception;

  UploadFolderAndPayResponse uploadFolderAndPay(
      String rootPath, int batchSize, String indexFile, String currency) throws Exception;

  ManifestUploadResponse uploadFolder(
      String rootPath, int batchSize, String indexFile, String currency, boolean needSequence)
      throws Exception;

  String submitNativeData(
      String apiKey, String currency, byte[] data, String contentType, Map<String, String> tags)
      throws Exception;

  BundleFee bundleFee(long size, String currency) throws Exception;

  List<BundleOrder> getOrders(String addr, int startId) throws Exception;
}
