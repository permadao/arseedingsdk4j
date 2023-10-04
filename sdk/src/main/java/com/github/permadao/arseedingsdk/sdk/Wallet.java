package com.github.permadao.arseedingsdk.sdk;

import com.github.permadao.model.wallet.KeyPair;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public interface Wallet {
  // 获取钱包地址
  String getAddress();

  // 获取钱包余额
  double getBalance();

  // 发起转账交易
  boolean sendTransaction(String toAddress, double amount);

  // 导出钱包私钥
  String exportPrivateKey();

  // 导出钱包公钥
  String exportPublicKey();

  // 获取秘钥对
  KeyPair getKeyPair();

  int signType();

  byte[] sign(byte[] msg);
}
