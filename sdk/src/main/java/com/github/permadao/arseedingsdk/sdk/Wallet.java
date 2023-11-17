package com.github.permadao.arseedingsdk.sdk;

import com.github.permadao.model.wallet.SignTypeEnum;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public interface Wallet {

  String getAddress();

  String getOwner();

  SignTypeEnum signType();

  byte[] sign(byte[] msg);

  String payTxSign(byte[] msg);
}
