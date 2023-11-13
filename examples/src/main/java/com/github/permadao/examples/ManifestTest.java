package com.github.permadao.examples;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.network.impl.HttpArSeedingService;
import com.github.permadao.arseedingsdk.sdk.ArSDK;
import com.github.permadao.arseedingsdk.sdk.Wallet;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.impl.EthereumWallet;
import com.github.permadao.arseedingsdk.sdk.response.UploadFolderAndPayResponse;
import okhttp3.OkHttpClient;

public class ManifestTest {
    public static void main(String[] args) throws Exception {
        String testDir = System.getProperty("user.dir") + "/dist";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.build();
        Wallet ethereumWallet = EthereumWallet.loadEthereumWallet(
                "cwtech78c954d792251666305c59526a71cb6eb70052cfb09556432fcf01eff838b0");
        System.out.println(ethereumWallet.getAddress());
        ArSeedingService arSeedingService =
                new HttpArSeedingService("https://seed-dev.everpay.io",
                        "https://api-dev.everpay.io", builder.build());
        ArSDK arSDK =
                ArHttpSDK.buildArHttpSDK(arSeedingService, ethereumWallet);
        UploadFolderAndPayResponse response = arSDK.uploadFolderAndPay(testDir, 2, "index.html", "usdt");

        System.out.println(response);
    }
}
