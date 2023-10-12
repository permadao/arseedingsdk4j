package com.github.permadao.arseedingsdk.sdk.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.codec.BundleItemBinary;
import com.github.permadao.arseedingsdk.codec.BundleItemSigner;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.util.AssertUtils;
import com.github.permadao.arseedingsdk.util.SHA256Utils;
import com.github.permadao.arseedingsdk.util.TagUtils;
import com.github.permadao.model.bundle.BundleItem;
import com.github.permadao.model.bundle.Tag;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/1 22:06
 */
public class DataSendRequest {

  private ArSeedingService arSeedingService;

  private Wallet wallet;

  protected final ObjectMapper objectMapper = new ObjectMapper();

  public DataSendRequest(ArSeedingService arSeedingService, Wallet wallet) {
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
  }

  public DataSendOrderResponse send(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence)
      throws Exception {

    verifyBase64DecodeResult(target);
    verifyBase64DecodeResult(anchor);

    byte[] tagsBytes = TagUtils.serializeTags(tags);

    BundleItem.Builder builder = new BundleItem.Builder();
    BundleItem bundleItem =
        builder
            .signatureType(wallet.signType().getValue())
            .signature(StringUtils.EMPTY)
            .owner(wallet.getAddress())
            .target(target)
            .anchor(anchor)
            .tags(tags)
            .id(StringUtils.EMPTY)
            .tagsBy(Base64Util.base64Encode(tagsBytes))
            .data(Base64Util.base64Encode(data))
            .build();

        byte[] bundleItemBytes = BundleItemSigner.bundleItemSignData(bundleItem);

        byte[] signedMsg = wallet.sign(bundleItemBytes);
        byte[] bytes = SHA256Utils.sha256(signedMsg);
        bundleItem.setId(Base64Util.base64Encode(bytes));
        bundleItem.setSignature(Base64Util.base64Encode(signedMsg));

    byte[] itemBinary = BundleItemBinary.generateItemBinary(bundleItem);

    String path = StringUtils.isBlank(currency) ? "/bundle/tx" :
        String.format("/bundle/tx/%s", currency);

    InputStream inputStream =
        arSeedingService.sendBytesRequestToArSeeding(path, itemBinary,
            buildHeaders(needSequence));

    return objectMapper.readValue(inputStream, DataSendOrderResponse.class);
  }

  private HashMap<String, String> buildHeaders(boolean needSequence){
    HashMap<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/octet-stream");
    if (needSequence) {
      headers.put("Sort", "true");
    }
    return headers;
  }

    private void verifyBase64DecodeResult(String data) {
        if (StringUtils.isEmpty(data)) {
            return;
        }
        byte[] bytes = Base64Util.base64Decode(data);
        AssertUtils.isTrue(bytes.length == 32, "decode result length must be 32");
    }
}
