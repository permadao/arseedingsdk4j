package com.github.permadao.arseedingsdk.util;

import com.github.permadao.exception.AssertException;

/**
 * @author shiwen.wy
 * @date 2023/10/3 23:06
 */
public class AssertUtils {
  public static void notNull(Object obj, String message) {
    if (obj == null) {
      throw new AssertException(message);
    }
  }

  public static void isTrue(boolean condition, String message) {
    if (!condition) {
      throw new AssertException(message);
    }
  }
}
