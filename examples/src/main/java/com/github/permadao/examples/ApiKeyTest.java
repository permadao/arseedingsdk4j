package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;
import com.github.permadao.model.bundle.Tag;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/11/18 11:34
 */
public class ApiKeyTest {

	public static void main(String[] args) throws Exception {
		Wallet ethereumWallet = EthereumWallet.loadEthereumWallet(
				"");
		ArSeedingService arSeedingService =
				new HttpArSeedingService("https://seed-dev.everpay.io",
						"https://api-dev.everpay.io");
		ArSDK arSDK =
				ArHttpSDK.buildArHttpSDK(arSeedingService, ethereumWallet);

		Map<String, String> tags = new HashMap<>();
		tags.put("1", "html");
		tags.put("2", "mp3");
		String s = arSDK.submitNativeData("", "usdc",
				"hello world".getBytes(),
				"text/html", tags);
		System.out.println(s);
	}
}
