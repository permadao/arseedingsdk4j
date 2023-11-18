package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.ArweaveWallet;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.model.bundle.Tag;
import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;

/**
 * @author shiwen.wy
 * @date 2023/11/12 16:41
 */
public class ArDataSendTest {

	public static void main(String[] args) throws Exception {
		OkHttpClient.Builder builder =
				new OkHttpClient.Builder();
		builder.build();
		Wallet arweaveWallet = ArweaveWallet.loadArWallet("your file path");
		System.out.println(arweaveWallet.getOwner());
		System.out.println(arweaveWallet.getAddress());
		ArSeedingService arSeedingService =
				new HttpArSeedingService("https://seed-dev.everpay.io",
						"https://api-dev.everpay.io",
						builder.build());
		ArSDK arSDK =
				ArHttpSDK.buildArHttpSDK(arSeedingService, arweaveWallet);

		Tag tag = new Tag("Content-Type", "video/mpeg4");
		Tag tag1 = new Tag("1", "html");
		Tag tag2 = new Tag("2", "mp3");
		Tag tag3 = new Tag("3", "mp3");

		DataSendResponse usdc =
				arSDK.sendData("hello world1111212".getBytes(), "usdc",
						Lists.newArrayList(tag, tag1, tag2, tag3), "", "", false);
		System.out.println(usdc);

		DataSendOrderResponse response =
				arSDK.sendDataAndPay("hello world1114qa".getBytes(), "usdc",
						Lists.newArrayList(tag, tag1, tag2, tag3), "", "", false);
		System.out.println(response);
	}
}
