package com.github.permadao.arseedingsdk.sdk.impl;

import com.github.permadao.model.wallet.KeyPair;
import com.github.permadao.arseedingsdk.sdk.Wallet;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:09
 */
public class EthereumWallet implements Wallet {

  @Override
  public String getAddress() {
    return null;
  }

  @Override
  public double getBalance() {
    return 0;
  }

  @Override
  public boolean sendTransaction(String toAddress, double amount) {
    return false;
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
  public int signType() {
    return 0;
  }

  @Override
  public byte[] sign(byte[] msg) {
    return new byte[0];
  }
}
