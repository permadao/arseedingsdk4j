package com.github.permadao.model.wallet;

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
}
