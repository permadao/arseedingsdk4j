package com.github.permadao.arseedingsdk.codec;

import com.github.permadao.model.bundle.BundleItem;
import com.github.permadao.model.wallet.SignTypeEnum;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;

/**
 * @author shiwen.wy
 * @date 2023/10/4 00:20
 */
public class BundleItemBinary {
  public static byte[] generateItemBinary(BundleItem bundleItem)
          throws Exception {

    int dataLength = 0;
    if (StringUtils.isNotBlank(bundleItem.getData())) {
      dataLength = Base64Util.base64Decode(bundleItem.getData()).length;
    }

    byte[] metaBinary = generateItemMetaBinary(bundleItem);
    byte[] by = new byte[metaBinary.length + dataLength];
    System.arraycopy(metaBinary, 0, by, 0, metaBinary.length);

    // push data
    if (dataLength > 0) {
      byte[] data = Base64Util.base64Decode(bundleItem.getData());
      System.arraycopy(data, 0, by, metaBinary.length, data.length);
    }

    return by;
  }

  private static byte[] generateItemMetaBinary(BundleItem bundleItem) throws Exception {

    byte[] targetBytes = new byte[0];
    if (StringUtils.isNotBlank(bundleItem.getTarget())) {
      targetBytes = Base64Util.base64Decode(bundleItem.getTarget());
    }

    byte[] anchorBytes = new byte[0];
    if (StringUtils.isNotBlank(bundleItem.getAnchor())) {
      anchorBytes = Base64Util.base64Decode(bundleItem.getAnchor());
    }

    byte[] tagsBytes = new byte[0];

    if (bundleItem.getTags() != null && !bundleItem.getTags().isEmpty()) {
      tagsBytes = Base64Util.base64Decode(bundleItem.getTagsBy());
    }

    SignTypeEnum signTypeEnum = SignTypeEnum.fromValue(bundleItem.getSignatureType());

    int sigLength = signTypeEnum.getSigConfig().getSigLength();
    int ownerLength = signTypeEnum.getSigConfig().getPubLength();

    // Create a byte array with a set length
    byte[] bytesArr = new byte[2 + sigLength + ownerLength];

    byte[] sig = Base64Util.base64Decode(bundleItem.getSignature());
    if (sig.length != sigLength) {
      throw new Exception("signature length incorrect");
    }

    byte[] ownerByte = Base64Util.base64Decode(bundleItem.getOwner());
    if (ownerByte.length != ownerLength) {
      throw new Exception("ownerByte length incorrect");
    }

    int currentIndex = 0;
    currentIndex =
        appendArray(bytesArr, currentIndex, shortTo2ByteArray(bundleItem.getSignatureType()));
    currentIndex = appendArray(bytesArr, currentIndex, sig);
    appendArray(bytesArr, currentIndex, ownerByte);

    // 2 + sigLength + ownerLength has used, so use concat here
    if (StringUtils.isNotBlank(bundleItem.getTarget())) {
      bytesArr = concatArrays(bytesArr, new byte[]{1});
      bytesArr = concatArrays(bytesArr, targetBytes);
    } else {
      bytesArr = concatArrays(bytesArr, new byte[]{0});
    }

    if (StringUtils.isNotBlank(bundleItem.getAnchor())) {
      bytesArr = concatArrays(bytesArr, new byte[]{1});
      bytesArr = concatArrays(bytesArr, anchorBytes);
    } else {
      bytesArr = concatArrays(bytesArr, new byte[]{0});
    }

    bytesArr = concatArrays(bytesArr,
            longTo8ByteArray(bundleItem.getTags() == null ? 0 :
                                     bundleItem.getTags().size()));
    bytesArr = concatArrays(bytesArr, longTo8ByteArray(tagsBytes.length));

    if (bundleItem.getTags() != null && !bundleItem.getTags().isEmpty()) {
      bytesArr = concatArrays(bytesArr, tagsBytes);
    }

    return bytesArr;
  }

  private static byte[] concatArrays(byte[] array1, byte[] array2) {
    byte[] result = new byte[array1.length + array2.length];
    System.arraycopy(array1, 0, result, 0, array1.length);
    System.arraycopy(array2, 0, result, array1.length, array2.length);
    return result;
  }

  private static int appendArray(byte[] destination, int destPos, byte[] source) {
    System.arraycopy(source, 0, destination, destPos, source.length);
    return destPos + source.length;
  }

  private static byte[] shortTo2ByteArray(int value) {
    byte[] byteArray = new byte[2];
    for (int i = 0; i < byteArray.length; i++) {
      byteArray[i] = (byte) (value & 0xFF);
      value >>= 8;
    }
    return byteArray;
  }

  private static byte[] longTo8ByteArray(long value) {
    byte[] byteArray = new byte[8];
    for (int i = 0; i < byteArray.length; i++) {
      byteArray[i] = (byte) (value & 0xFF);
      value >>= 8;
    }
    return byteArray;
  }
}
