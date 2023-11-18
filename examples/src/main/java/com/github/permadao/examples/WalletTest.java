package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;

import java.io.File;

/**
 * @author shiwen.wy
 * @date 2023/10/17 23:40
 */
public class WalletTest {
  public static void main(String[] args) throws Exception {
    test1();
    System.out.println("-----------------------");
    test2();
  }

  private static void test2() throws Exception {
    String fileName = "your file";
    EthereumWallet ethereumWallet =
        EthereumWallet.loadEthereumWallet("your password", new File(fileName));
    System.out.println(ethereumWallet.getAddress());
  }

  private static void test1() {
    EthereumWallet ethereumWallet =
        EthereumWallet.loadEthereumWallet(
            "your private key");
    System.out.println(ethereumWallet.getAddress());
  }
}
