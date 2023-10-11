package com.github.permadao.model.wallet;

/**
 * @author shiwen.wy
 * @date 2023/10/8 00:35
 */
public enum SignTypeEnum {
    ARWEAVE(1),
    ED25519(2),
    ETHEREUM(3),
    SOLANA(4);

    private final int value;

    SignTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SignTypeEnum fromValue(int value) {
        for (SignTypeEnum type : SignTypeEnum.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SignType value: " + value);
    }
}
