package com.github.permadao.arseedingsdk.codec;

import com.github.permadao.model.bundle.BundleItem;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.security.NoSuchAlgorithmException;

/**
 * @author shiwen.wy
 * @date 2023/10/3 23:44
 */
public class BundleItemSigner {
    public static byte[] bundleItemSignData(BundleItem bundleItem) throws NoSuchAlgorithmException {
        List<Object> dataList = new ArrayList<>();
        dataList.add(Base64Util.base64Encode("dataitem".getBytes(StandardCharsets.UTF_8)));
        dataList.add(Base64Util.base64Encode("1".getBytes(StandardCharsets.UTF_8)));
        dataList.add(Base64Util.base64Encode(Integer.toString(bundleItem.getSignatureType()).getBytes(
            StandardCharsets.UTF_8)));
        dataList.add(bundleItem.getOwner());
        dataList.add(bundleItem.getTarget());
        dataList.add(bundleItem.getAnchor());
        dataList.add(bundleItem.getTagsBy());

        dataList.add(bundleItem.getData());

        byte[] hash = DeepHashCalculator.deepHash(dataList);
        return hash;
    }

}
