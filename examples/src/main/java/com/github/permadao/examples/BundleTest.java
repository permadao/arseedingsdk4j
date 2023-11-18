package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;
import com.github.permadao.model.bundle.BundleFee;
import com.github.permadao.model.bundle.BundleOrder;

import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/11/18 11:02
 */
public class BundleTest {
	public static void main(String[] args) throws Exception {
		Wallet ethereumWallet = EthereumWallet.loadEthereumWallet(
				"");
		System.out.println(ethereumWallet.getAddress());
		ArSeedingService arSeedingService =
				new HttpArSeedingService("https://seed-dev.everpay.io",
						"https://api-dev.everpay.io");
		ArSDK arSDK =
				ArHttpSDK.buildArHttpSDK(arSeedingService, ethereumWallet);

		BundleFee bundleFee = arSDK.bundleFee(10, "usdc");

		System.out.println("bundleFee is " + bundleFee);

		List<BundleOrder> orders =
				arSDK.getOrders("",
						0);

		System.out.println("orders is " + orders);
	}
}
