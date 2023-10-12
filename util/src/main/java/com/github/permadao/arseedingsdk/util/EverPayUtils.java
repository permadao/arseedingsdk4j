package com.github.permadao.arseedingsdk.util;

/**
 * @author shiwen.wy
 * @date 2023/10/9 23:54
 */
public class EverPayUtils {
    private static long lastNonce = 0;
    public static synchronized long getNonce() {
        while (true) {
            long newNonce = System.nanoTime() / 1000000;
            if (newNonce > lastNonce) {
                lastNonce = newNonce;
                return newNonce;
            }
        }
    }
}
