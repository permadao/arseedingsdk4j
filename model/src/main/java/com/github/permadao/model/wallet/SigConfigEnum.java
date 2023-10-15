package com.github.permadao.model.wallet;

/**
 * @author shiwen.wy
 * @date 2023/10/16 00:28
 */
public enum SigConfigEnum {
  ARWEAVE_SIGN_TYPE(512, 512, "arweave"),
  ED25519_SIGN_TYPE(64, 32, "ed25519"),
  ETHEREUM_SIGN_TYPE(65, 65, "ethereum"),
  SOLANA_SIGN_TYPE(64, 32, "solana");

  private final int sigLength;
  private final int pubLength;
  private final String sigName;

  SigConfigEnum(int sigLength, int pubLength, String sigName) {
    this.sigLength = sigLength;
    this.pubLength = pubLength;
    this.sigName = sigName;
  }

  public int getSigLength() {
    return sigLength;
  }

  public int getPubLength() {
    return pubLength;
  }

  public String getSigName() {
    return sigName;
  }
}
