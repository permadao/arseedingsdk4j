package com.github.permadao.arseedingsdk.sdk.impl;

import com.github.permadao.model.wallet.KeyPair;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.model.wallet.SignTypeEnum;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public class ArweaveWallet implements Wallet {

  @Override
  public String getAddress() {
    return null;
  }

  @Override
  public String exportPrivateKey() {
    return null;
  }

  @Override
  public String exportPublicKey() {
    return null;
  }

  @Override
  public KeyPair getKeyPair() {
    return null;
  }

  @Override
  public SignTypeEnum signType() {
    return SignTypeEnum.ARWEAVE;
  }

  @Override
  public byte[] sign(byte[] msg) {
    return new byte[0];
  }
}
