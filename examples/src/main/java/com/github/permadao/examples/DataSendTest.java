package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.model.bundle.Tag;
import com.google.common.collect.Lists;

/**
 * @author shiwen.wy
 * @date 2023/10/18 00:04
 */
public class DataSendTest {
	public static void main(String[] args) throws Exception {
		Wallet ethereumWallet = EthereumWallet.loadEthereumWallet(
				"your private key");
		System.out.println(ethereumWallet.getAddress());
		ArSeedingService arSeedingService =
				new HttpArSeedingService("https://seed-dev.everpay.io",
						"https://api-dev.everpay.io");
		ArSDK arSDK =
				ArHttpSDK.buildArHttpSDK(arSeedingService, ethereumWallet);

		Tag tag = new Tag("Content-Type", "video/mpeg4");

		DataSendOrderResponse usdc =
				arSDK.sendDataAndPay("hello world1".getBytes(), "usdc",
						Lists.newArrayList(tag), "", "", false);
		System.out.println(usdc);
	}
}
