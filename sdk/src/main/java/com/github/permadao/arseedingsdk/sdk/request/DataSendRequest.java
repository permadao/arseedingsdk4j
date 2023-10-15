package com.github.permadao.arseedingsdk.sdk.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.codec.BundleItemBinary;
import com.github.permadao.arseedingsdk.codec.BundleItemSigner;
import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.arseedingsdk.util.AssertUtils;
import com.github.permadao.arseedingsdk.util.SHA256Utils;
import com.github.permadao.arseedingsdk.util.TagUtils;
import com.github.permadao.model.bundle.BundleItem;
import com.github.permadao.model.bundle.Tag;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import static com.github.permadao.model.constant.UrlPathContant.DATA_SEND_TX_PATH;
import static com.github.permadao.model.constant.UrlPathContant.DATA_SEND_TX_WITH_CURRENCY_PATH;

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

  public DataSendResponse send(
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

    BundleItem bundleItem = buildBundleItem(target, anchor, tags, tagsBytes, data);

    byte[] itemBinary = BundleItemBinary.generateItemBinary(bundleItem);

    String path = getPath(currency);

    InputStream inputStream =
        arSeedingService.sendBytesRequestToArSeeding(path, itemBinary, buildHeaders(needSequence));

    return objectMapper.readValue(inputStream, DataSendResponse.class);
  }

  private String getPath(String currency) {
    return StringUtils.isBlank(currency)
        ? DATA_SEND_TX_PATH
        : String.format(DATA_SEND_TX_WITH_CURRENCY_PATH, currency);
  }

  private BundleItem buildBundleItem(
      String target, String anchor, List<Tag> tags, byte[] tagsBytes, byte[] data)
      throws NoSuchAlgorithmException {
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

    return bundleItem;
  }

  private HashMap<String, String> buildHeaders(boolean needSequence) {
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
