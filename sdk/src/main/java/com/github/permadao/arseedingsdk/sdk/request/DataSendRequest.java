package com.github.permadao.arseedingsdk.sdk.request;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.model.wallet.Wallet;

import java.util.Map;

/**
 * @author shiwen.wy
 * @date 2023/10/1 22:06
 */
public class DataSendRequest {

    private ArSeedingService arSeedingService;

    private Wallet wallet;

    public DataSendRequest(ArSeedingService arSeedingService, Wallet wallet) {
        this.arSeedingService = arSeedingService;
        this.wallet = wallet;
    }

    public DataSendResponse send(byte[] data, String currency,
        Map<String, String> tags) {

        return null;
    }
}
