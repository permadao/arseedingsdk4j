package com.github.permadao.model.wallet;

/**
 * @author shiwen.wy
 * @date 2023/10/8 00:35
 */
public enum SignTypeEnum {
    ARWEAVE(1, SigConfigEnum.ARWEAVE_SIGN_TYPE),
    ED25519(2, SigConfigEnum.ED25519_SIGN_TYPE),
    ETHEREUM(3, SigConfigEnum.ETHEREUM_SIGN_TYPE),
    SOLANA(4, SigConfigEnum.SOLANA_SIGN_TYPE);

    private final int value;
    private final SigConfigEnum sigConfig;

    SignTypeEnum(int value, SigConfigEnum sigConfig) {
        this.value = value;
        this.sigConfig = sigConfig;
    }

    public int getValue() {
        return value;
    }

    public SigConfigEnum getSigConfig() {
        return sigConfig;
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
