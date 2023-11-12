package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.model.bundle.Tag;
import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;

/**
 * @author shiwen.wy
 * @date 2023/10/18 00:04
 */
public class DataSendTest {
	public static void main(String[] args) throws Exception {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.build();
		Wallet ethereumWallet = EthereumWallet.loadEthereumWallet(
				"1d8bdd0d2f1e73dffe1111111118325b7e195669541f76559760ef615a588be3");
		System.out.println(ethereumWallet.getAddress());
		ArSeedingService arSeedingService =
				new HttpArSeedingService("https://seed-dev.everpay.io",
						"https://api-dev.everpay.io", builder.build());
		ArSDK arSDK =
				ArHttpSDK.buildArHttpSDK(arSeedingService, ethereumWallet);

		Tag tag = new Tag("Content-Type", "video/mpeg4");

		DataSendOrderResponse usdc =
				arSDK.sendDataAndPay("hello world1".getBytes(), "usdc",
						Lists.newArrayList(tag), "", "", false);
		System.out.println(usdc);
	}
}
