package com.github.permadao.arseedingsdk.sdk.impl.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.codec.BundleItemBinary;
import com.github.permadao.arseedingsdk.sdk.Manifest;
import com.github.permadao.model.Transaction;
import com.github.permadao.model.bundle.BundleItem;
import com.github.permadao.model.bundle.Tag;
import com.github.permadao.model.manifest.ManifestResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ManifestImpl implements Manifest {
    private KVDBService kvdbService;

    private ManifestImpl(KVDBService kvdbService) {
        this.kvdbService = kvdbService;
    }

    public byte[] getArTxOrItemData(String id, List<Tag> decodeTags) throws Exception {
        byte[] bytes = kvdbService.LoadItemBinary(id);
        if (bytes != null && bytes.length > 0) {
            return getBundleItemData(id, decodeTags);
        }
        Transaction transaction = kvdbService.LoadTxMeta(id);
        byte[] data = ApiMethod.txDataByMeta(transaction);
        decodeTags.addAll(transaction.getTags());
        return data;
    }

    public ManifestResponse getArTxOrItemDataForManifest() {
        return null;
    }

    public ManifestResponse getArTxOrItemTags() {
        return null;
    }

    public byte[] getBundleItemData(String id, List<Tag> decodeTags) throws Exception {
        byte[] itemBinary = kvdbService.LoadItemBinary(id);
        try (InputStream binaryStream = new ByteArrayInputStream(itemBinary)) {
            BundleItem bundleItem = BundleItemBinary.parseBundleItem(binaryStream);
            decodeTags.addAll(bundleItem.getTags());
            return Base64Util.base64Decode(bundleItem.getData());
        }
    }

    public ManifestResponse parseBundleItem() {
        return null;
    }

    public byte[] handleManifest() throws Exception {

        return null;
    }

    public void syncManifestData() throws Exception {

    }


    public String getRawById(String id, byte[] data) throws Exception {
        return null;
    }
}
