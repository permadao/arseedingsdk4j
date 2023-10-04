package com.github.permadao.arseedingsdk.sdk.impl;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.request.DataSendRequest;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:54
 */
public class ArHttpSDK implements ArSDK {
  private ArSeedingService arSeedingService;
  private Wallet wallet;

  private ArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
    this.arSeedingService = arSeedingService;
    this.wallet = wallet;
  }

  public static ArSDK buildArHttpSDK(ArSeedingService arSeedingService, Wallet wallet) {
    return new ArHttpSDK(arSeedingService, wallet);
  }
}
