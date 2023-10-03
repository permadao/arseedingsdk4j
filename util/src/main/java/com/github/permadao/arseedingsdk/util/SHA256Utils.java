package com.github.permadao.arseedingsdk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author shiwen.wy
 * @date 2023/10/3 23:55
 */
public class SHA256Utils {
    public static byte[] sha384(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-384 not available", e);
        }
    }

    public static byte[] sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
