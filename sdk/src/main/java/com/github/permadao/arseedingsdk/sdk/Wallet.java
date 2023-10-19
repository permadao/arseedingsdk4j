package com.github.permadao.arseedingsdk.sdk;

import com.github.permadao.model.wallet.SignTypeEnum;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public interface Wallet {
  // 获取钱包地址
  String getAddress();
  String getOwner();
  // 导出钱包私钥
  String exportPrivateKey();

  // 导出钱包公钥
  String exportPublicKey();

  SignTypeEnum signType();

  byte[] sign(byte[] msg);

  String payTxSign(byte[] msg);
}
