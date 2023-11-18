package com.github.permadao.arseedingsdk.codec;


import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author shiwen.wy
 * @date 2023/10/3 22:40
 */
public class Base64Util {
  public static String base64Encode(byte[] data) {
    byte[] encodedBytes = Base64.encodeBase64URLSafe(data);
    return new String(encodedBytes, StandardCharsets.UTF_8);
  }

  public static byte[] base64Decode(String data) {
    return Base64.decodeBase64(data);
  }
}
