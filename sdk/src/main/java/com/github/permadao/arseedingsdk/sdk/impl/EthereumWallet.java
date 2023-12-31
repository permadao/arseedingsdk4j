package com.github.permadao.arseedingsdk.sdk.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.codec.Base64Util;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.model.wallet.SignTypeEnum;
import com.github.permadao.model.wallet.WalletFile;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.params.KeyParameter;
import org.web3j.crypto.*;

import org.web3j.utils.Numeric;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:09
 */
public class EthereumWallet implements Wallet {

  private final ECKeyPair ecKeyPair;
  private final String address;

  private EthereumWallet(ECKeyPair ecKeyPair, String address) {
    this.ecKeyPair = ecKeyPair;
    this.address = address;
  }

  private static final int CURRENT_VERSION = 3;
  private static final String CIPHER = "aes-128-ctr";
  static final String AES_128_CTR = "pbkdf2";
  static final String SCRYPT = "scrypt";

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static EthereumWallet loadEthereumWallet(String password, File source)
      throws Exception {
    WalletFile walletFile = objectMapper.readValue(source, WalletFile.class);
    ECKeyPair ecKeyPair = decrypt(password, walletFile);
    String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
    return new EthereumWallet(ecKeyPair, address);
  }

  public static EthereumWallet loadEthereumWallet(String privateKey) {
    ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
    String address = Numeric.prependHexPrefix(Keys.getAddress(ecKeyPair));
    return new EthereumWallet(ecKeyPair, address);
  }

  public static ECKeyPair decrypt(String password, WalletFile walletFile) throws CipherException {

    validate(walletFile);

    WalletFile.Crypto crypto = walletFile.getCrypto();

    byte[] mac = Numeric.hexStringToByteArray(crypto.getMac());
    byte[] iv = Numeric.hexStringToByteArray(crypto.getCipherparams().getIv());
    byte[] cipherText = Numeric.hexStringToByteArray(crypto.getCiphertext());

    byte[] derivedKey;

    WalletFile.KdfParams kdfParams = crypto.getKdfparams();
    if (kdfParams instanceof WalletFile.ScryptKdfParams) {
      WalletFile.ScryptKdfParams scryptKdfParams =
          (WalletFile.ScryptKdfParams) crypto.getKdfparams();
      int dklen = scryptKdfParams.getDklen();
      int n = scryptKdfParams.getN();
      int p = scryptKdfParams.getP();
      int r = scryptKdfParams.getR();
      byte[] salt = Numeric.hexStringToByteArray(scryptKdfParams.getSalt());
      derivedKey = generateDerivedScryptKey(password.getBytes(UTF_8), salt, n, r, p, dklen);
    } else if (kdfParams instanceof WalletFile.Aes128CtrKdfParams) {
      WalletFile.Aes128CtrKdfParams aes128CtrKdfParams =
          (WalletFile.Aes128CtrKdfParams) crypto.getKdfparams();
      int c = aes128CtrKdfParams.getC();
      String prf = aes128CtrKdfParams.getPrf();
      byte[] salt = Numeric.hexStringToByteArray(aes128CtrKdfParams.getSalt());

      derivedKey = generateAes128CtrDerivedKey(password.getBytes(UTF_8), salt, c, prf);
    } else {
      throw new CipherException("Unable to deserialize params: " + crypto.getKdf());
    }

    byte[] derivedMac = generateMac(derivedKey, cipherText);

    if (!Arrays.equals(derivedMac, mac)) {
      throw new CipherException("Invalid password provided");
    }

    byte[] encryptKey = Arrays.copyOfRange(derivedKey, 0, 16);
    byte[] privateKey = performCipherOperation(Cipher.DECRYPT_MODE, iv, encryptKey, cipherText);
    return ECKeyPair.create(privateKey);
  }

  private static byte[] performCipherOperation(int mode, byte[] iv, byte[] encryptKey, byte[] text)
      throws CipherException {

    try {
      IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
      Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

      SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");
      cipher.init(mode, secretKeySpec, ivParameterSpec);
      return cipher.doFinal(text);
    } catch (NoSuchPaddingException
        | NoSuchAlgorithmException
        | InvalidAlgorithmParameterException
        | InvalidKeyException
        | BadPaddingException
        | IllegalBlockSizeException e) {
      throw new CipherException("Error performing cipher operation", e);
    }
  }

  private static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
    byte[] result = new byte[16 + cipherText.length];

    System.arraycopy(derivedKey, 16, result, 0, 16);
    System.arraycopy(cipherText, 0, result, 16, cipherText.length);

    return Hash.sha3(result);
  }

  private static byte[] generateDerivedScryptKey(
      byte[] password, byte[] salt, int n, int r, int p, int dkLen) throws CipherException {
    return SCrypt.generate(password, salt, n, r, p, dkLen);
  }

  private static byte[] generateAes128CtrDerivedKey(byte[] password, byte[] salt, int c, String prf)
      throws CipherException {

    if (!prf.equals("hmac-sha256")) {
      throw new CipherException("Unsupported prf:" + prf);
    }

    // Java 8 supports this, but you have to convert the password to a character array, see
    // http://stackoverflow.com/a/27928435/3211687

    PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA256Digest());
    gen.init(password, salt, c);
    return ((KeyParameter) gen.generateDerivedParameters(256)).getKey();
  }

  static void validate(WalletFile walletFile) throws CipherException {
    WalletFile.Crypto crypto = walletFile.getCrypto();

    if (walletFile.getVersion() != CURRENT_VERSION) {
      throw new CipherException("Wallet version is not supported");
    }

    if (!crypto.getCipher().equals(CIPHER)) {
      throw new CipherException("Wallet cipher is not supported");
    }

    if (!crypto.getKdf().equals(AES_128_CTR) && !crypto.getKdf().equals(SCRYPT)) {
      throw new CipherException("KDF type is not supported");
    }
  }

  @Override
  public String getAddress() {
    return this.address;
  }

  @Override
  public String getOwner() {
    byte[] byteArray = ecKeyPair.getPublicKey().toByteArray();
    if (byteArray.length == 64) {
      byte[] bytes = new byte[65];
      bytes[0] = (byte)4;
      System.arraycopy(byteArray, 0, bytes, 1, 64);
      return Base64Util.base64Encode(bytes);
    }
    byteArray[0] = 0x04;
    return Base64Util.base64Encode(byteArray);
  }

  @Override
  public SignTypeEnum signType() {
    return SignTypeEnum.ETHEREUM;
  }

  @Override
  public byte[] sign(byte[] msg) {
    Sign.SignatureData signatureData = Sign.signPrefixedMessage(msg, ecKeyPair);
    return formatSignature(signatureData.getR(), signatureData.getS(),
            (byte)((int)(signatureData.getV()[0]) - 27));
  }

  private byte[] formatSignature(byte[] r, byte[] s, byte v) {
    if (r.length != 32 || s.length != 32 || (v != 0 && v != 1)) {
      throw new IllegalArgumentException("Invalid input data");
    }

    byte[] signature = new byte[65];
    System.arraycopy(r, 0, signature, 0, 32);
    System.arraycopy(s, 0, signature, 32, 32);
    signature[64] = v;

    return signature;
  }

  @Override
  public String payTxSign(byte[] msg) {
    byte[] sig = sign(msg);
    sig[64]+=27;
    return Numeric.toHexString(sig);
  }
}
