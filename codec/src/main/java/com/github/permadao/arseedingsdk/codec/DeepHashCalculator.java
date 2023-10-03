package com.github.permadao.arseedingsdk.codec;

import com.github.permadao.arseedingsdk.util.SHA256Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
/**
 * @author shiwen.wy
 * @date 2023/10/3 23:41
 */
public class DeepHashCalculator {

    public static byte[] deepHash(List<Object> data) throws NoSuchAlgorithmException {
        byte[] tag = Objects.requireNonNull(calculateTag(data));
        return deepHashChunk(data, tag);
    }

    private static byte[] calculateTag(List<Object> data) {
        byte[] tag = ("list" + data.size()).getBytes(StandardCharsets.UTF_8);
        return SHA256Utils.sha384(tag);
    }

    private static byte[] deepHashChunk(List<Object> data, byte[] acc) throws NoSuchAlgorithmException {
        if (data.isEmpty()) {
            return acc;
        }

        byte[] dHash;
        Object firstItem = data.get(0);

        if (firstItem instanceof InputStream) {
            dHash = deepHashStream((InputStream) firstItem);
        } else if (firstItem instanceof String) {
            dHash = deepHashStr((String) firstItem);
        } else if (firstItem instanceof List<?>) {
            dHash = deepHash((List<Object>) firstItem);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + firstItem.getClass());
        }

        byte[] hashPair = new byte[acc.length + dHash.length];
        System.arraycopy(acc, 0, hashPair, 0, acc.length);
        System.arraycopy(dHash, 0, hashPair, acc.length, dHash.length);

        return deepHashChunk(data.subList(1, data.size()),
            SHA256Utils.sha384(hashPair));
    }

    private static byte[] deepHashStream(InputStream data) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = data.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return digest.digest();
        } catch (IOException e) {
            throw new RuntimeException("Error reading data stream", e);
        }
    }

    private static byte[] deepHashStr(String data) throws NoSuchAlgorithmException {
        byte[] decodedData = Base64Util.base64Decode(data);
        byte[] tag = ("blob" + decodedData.length).getBytes(StandardCharsets.UTF_8);
        byte[] tagHash = SHA256Utils.sha384(tag);
        byte[] blobHash = SHA256Utils.sha384(decodedData);
        byte[] tagged = new byte[tagHash.length + blobHash.length];
        System.arraycopy(tagHash, 0, tagged, 0, tagHash.length);
        System.arraycopy(blobHash, 0, tagged, tagHash.length, blobHash.length);
        return SHA256Utils.sha384(tagged);
    }
}
