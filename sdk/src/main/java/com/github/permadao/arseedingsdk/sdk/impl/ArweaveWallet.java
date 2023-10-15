package com.github.permadao.arseedingsdk.sdk.impl;

import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.util.SHA256Utils;
import com.github.permadao.model.wallet.SignTypeEnum;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.web3j.crypto.Sign;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public class ArweaveWallet implements Wallet {

  private final KeyPair keyPair;

  public ArweaveWallet(KeyPair keyPair) {
    this.keyPair = keyPair;
  }

  public static ArweaveWallet loanArweaveWallet(String privateKey) {
    return new ArweaveWallet(generateKeyPairFromPrivateKey(privateKey));
  }

  private static KeyPair generateKeyPairFromPrivateKey(String privateKeyBase64) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);

      PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

      // Create a PublicKey from the PrivateKey (not secure for all cases)
      // This is typically not recommended, but it's possible in some scenarios.
      java.security.spec.X509EncodedKeySpec publicKeySpec =
          new java.security.spec.X509EncodedKeySpec(privateKey.getEncoded());
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

      return new KeyPair(publicKey, privateKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public String getAddress() {
    PublicKey publicKey = keyPair.getPublic();
    byte[] publicKeyBytes = publicKey.getEncoded();
    return Base64.getEncoder().encodeToString(publicKeyBytes);
  }

  @Override
  public String exportPrivateKey() {
    PrivateKey privateKey = keyPair.getPrivate();
    byte[] privateKeyBytes = privateKey.getEncoded();
    return Base64.getEncoder().encodeToString(privateKeyBytes);
  }

  @Override
  public String exportPublicKey() {
    return getAddress();
  }

  @Override
  public SignTypeEnum signType() {
    return SignTypeEnum.ARWEAVE;
  }

  @Override
  public byte[] sign(byte[] msg) {
    try {
      Signature signature = Signature.getInstance("SHA256withRSA");
      signature.initSign(keyPair.getPrivate());
      signature.update(msg);
      return signature.sign();
    } catch (Exception e) {
      // TODO
    }
    return null;
  }

  @Override
  public String payTxSign(byte[] msg) {
    byte[] textHash = Sign.getEthereumMessageHash(msg);
    byte[] hashedMessage = SHA256Utils.sha256(textHash);

    Signature signature = null;
    try {
      signature = Signature.getInstance("SHA256withRSA");
      signature.initSign(keyPair.getPrivate());
      // 使用私钥对消息进行签名
      signature.update(hashedMessage);
      byte[] signatureBytes = signature.sign();
      RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
      return Base64Util.base64Encode(signatureBytes)
          + ","
          + Base64Util.base64Encode(publicKey.getModulus().toByteArray());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
