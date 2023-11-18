package com.github.permadao.arseedingsdk.util;

/**
 * @author shiwen.wy
 * @date 2023/10/10 01:19
 */
public class HexEncoderUtils {
    private static final char[] HEX_TABLE = "0123456789abcdef".toCharArray();

    public static String encode(byte[] src) {
        byte[] dst = new byte[src.length * 2];
        hexEncode(dst, src);
        return "0x" + new String(dst);
    }

    public static int hexEncode(byte[] dst, byte[] src) {
        int j = 0;
        for (byte v : src) {
            dst[j] = (byte) HEX_TABLE[v >> 4];
            dst[j + 1] = (byte) HEX_TABLE[v & 0x0F];
            j += 2;
        }
        return src.length * 2;
    }
}
