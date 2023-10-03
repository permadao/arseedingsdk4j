package com.github.permadao.arseedingsdk.network.impl;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.github.permadao.exception.ConnectionException;

/**
 * @author shiwen.wy
 * @date 2023/10/1 14:16
 */
public class HttpArSeedingService implements ArSeedingService {

    private String arSeedingUrl;

    private String payUrl;

    private OkHttpClient httpClient;

    public static final MediaType JSON_MEDIA_TYPE =
        MediaType.parse("application/json; charset=utf-8");

    public HttpArSeedingService(String arSeedingUrl, String payUrl,
        OkHttpClient httpClient) {
        this.arSeedingUrl = arSeedingUrl;
        this.payUrl = payUrl;
        this.httpClient = httpClient;
    }

    @Override public InputStream sendJsonRequestToArSeeding(String request,
        HashMap<String, String> headers) throws IOException {
        RequestBody requestBody = RequestBody.create(request, JSON_MEDIA_TYPE);
        Headers httpHeaders = Headers.of(headers);

        okhttp3.Request httpRequest =
            new okhttp3.Request.Builder().url(arSeedingUrl).headers(httpHeaders)
                .post(requestBody).build();

        try (okhttp3.Response response = httpClient.newCall(httpRequest)
            .execute()) {
            ResponseBody responseBody = response.body();
            if (response.isSuccessful()) {
                if (responseBody != null) {
                    return new ByteArrayInputStream(responseBody.bytes());
                } else {
                    return null;
                }
            } else {
                int code = response.code();
                String text =
                    responseBody == null ? "N/A" : responseBody.string();

                throw new ConnectionException(
                    "Invalid response received: " + code + "; " + text);
            }
        }
    }
}
