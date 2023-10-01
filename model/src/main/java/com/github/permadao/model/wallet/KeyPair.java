package com.github.permadao.model.wallet;

import java.math.BigInteger;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:29
 */
public class KeyPair {

    private final BigInteger privateKey;
    private final BigInteger publicKey;

    public KeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public byte[] sign(byte[] msg) {
        // TODO
        return null;
    }

    public boolean verify(byte[] msg) {
        // TODO
        return false;
    }

    public static KeyPair create(BigInteger privateKey) {
        // TODO
        return null;
    }
}
