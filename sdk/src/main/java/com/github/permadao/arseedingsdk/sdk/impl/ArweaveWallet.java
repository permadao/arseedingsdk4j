package com.github.permadao.arseedingsdk.sdk.impl;

import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.util.SHA256Utils;
import com.github.permadao.model.wallet.SignTypeEnum;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.Arrays;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:08
 */
public class ArweaveWallet implements Wallet {
  private static final Logger log =
          LoggerFactory.getLogger(ArweaveWallet.class);
  private static final String SIGNATURE_ALGORITHM = "SHA256withRSA/PSS";
  private static final int SIGNATURE_SALT_LENGTH = 32;

  private BigInteger n;
  private int e;
  private String address;
  private PrivateKey privateKey;

  public ArweaveWallet(BigInteger n, int e, String address,
          PrivateKey privateKey) {
    this.n = n;
    this.e = e;
    this.address = address;
    this.privateKey = privateKey;
  }

  public static ArweaveWallet loadArWallet(String filePath) throws Exception {
    byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
    Security.addProvider(new BouncyCastleProvider());
    try {
      String keyString = new String(keyBytes, StandardCharsets.UTF_8);
      JSONObject jsonKey = new JSONObject(keyString);

      String n = jsonKey.getString("n");
      String e = jsonKey.getString("e");
      String d = jsonKey.getString("d");

      byte[] data = safeDecode(e);

      if (data.length < 4) {
        byte[] ndata = new byte[4];
        System.arraycopy(data, 0, ndata, 4 - data.length, data.length);
        data = ndata;
      }
      int publicKey_e = toInt(data);

      data = safeDecode(n);

      BigInteger publicKey_n = new BigInteger(1, data);

      byte[] bytes = SHA256Utils.sha256(publicKey_n.toByteArray());
      String address = Base64Util.base64Encode(bytes);

      String p = jsonKey.getString("p");
      String q = jsonKey.getString("q");
      String dp = jsonKey.getString("dp");
      String dq = jsonKey.getString("dq");
      String qi = jsonKey.getString("qi");

      BigInteger privateExponent =
              new BigInteger(1, Base64Util.base64Decode(d));
      BigInteger primeP = new BigInteger(1, Base64Util.base64Decode(p));
      BigInteger primeQ = new BigInteger(1, Base64Util.base64Decode(q));
      BigInteger primeExponentP =
              new BigInteger(1, Base64Util.base64Decode(dp));
      BigInteger primeExponentQ =
              new BigInteger(1, Base64Util.base64Decode(dq));
      BigInteger crtCoefficient =
              new BigInteger(1, Base64Util.base64Decode(qi));

      BigInteger modulus = new BigInteger(1, Base64Util.base64Decode(n));
      BigInteger publicExponent =
              new BigInteger(1, Base64Util.base64Decode(e));

      RSAPrivateCrtKeySpec privateKeySpec =
              new RSAPrivateCrtKeySpec(modulus, publicExponent,
                      privateExponent, primeP, primeQ, primeExponentP,
                      primeExponentQ, crtCoefficient);

      KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

      PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

      return new ArweaveWallet(publicKey_n, publicKey_e, address, privateKey);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static int toInt(byte[] data) {
    // Ensure that the byte array has at least 4 bytes
    if (data.length < 4) {
      throw new IllegalArgumentException(
              "Byte array must have at least 4 bytes");
    }

    ByteBuffer buffer = ByteBuffer.wrap(data);
    buffer.order(java.nio.ByteOrder.BIG_ENDIAN);

    return buffer.getInt();
  }

  private static byte[] safeDecode(String str) {
    int lenMod4 = str.length() % 4;
    if (lenMod4 > 0) {
      str = str + "====".substring(lenMod4);
    }

    return Base64Util.base64Decode(str);
  }

  @Override
  public String getAddress() {
    return address;
  }

  @Override
  public String getOwner() {
    return Base64Util.base64Encode(removeFirstByte(n.toByteArray()));
  }

  private static byte[] removeFirstByte(byte[] input) {
    if (input == null || input.length <= 1) {
      return new byte[0];  // Return an empty array if the input is null or has only one byte
    }

    return Arrays.copyOfRange(input, 1, input.length);
  }

  @Override
  public String exportPrivateKey() {
    return null;
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
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM,
              BouncyCastleProvider.PROVIDER_NAME);
      PSSParameterSpec pssParameterSpec = new PSSParameterSpec(
              MGF1ParameterSpec.SHA256.getDigestAlgorithm(), "MGF1",
              MGF1ParameterSpec.SHA256, SIGNATURE_SALT_LENGTH, 1);
      signature.setParameter(pssParameterSpec);
      signature.initSign(privateKey);
      signature.update(msg);
      return signature.sign();
    } catch (Exception e) {
      throw new RuntimeException("Error signing message", e);
    }
  }

  @Override
  public String payTxSign(byte[] msg) {
    try {
      byte[] signatureBytes = sign(msg);
      return Base64Util.base64Encode(signatureBytes)
          + ","
          + getOwner();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
