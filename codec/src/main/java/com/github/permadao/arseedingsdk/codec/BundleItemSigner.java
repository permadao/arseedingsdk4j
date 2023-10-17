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

    private static final String DATA_ITEM = "dataitem";

    private static final String PREFIX_1 = "1";

    public static byte[] bundleItemSignData(BundleItem bundleItem) throws NoSuchAlgorithmException {
        List<Object> dataList = new ArrayList<>();
        dataList.add(Base64Util.base64Encode(DATA_ITEM.getBytes(StandardCharsets.UTF_8)));
        dataList.add(Base64Util.base64Encode(PREFIX_1.getBytes(StandardCharsets.UTF_8)));
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
