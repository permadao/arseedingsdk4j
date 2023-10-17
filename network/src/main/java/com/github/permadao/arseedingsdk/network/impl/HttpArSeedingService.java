package com.github.permadao.arseedingsdk.network.impl;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.github.permadao.exception.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http client connent to arseeding or everpay
 *
 * @author shiwen.wy
 * @date 2023/10/1 14:16
 */
public class HttpArSeedingService implements ArSeedingService {

  private static final Logger log = LoggerFactory.getLogger(HttpArSeedingService.class);

  private final String arSeedingHost;

  private final String payHost;

  private final OkHttpClient httpClient;

  public static final MediaType JSON_MEDIA_TYPE =
          MediaType.parse("application/json; charset=utf-8");

  public static final MediaType BYTE_MEDIA_TYPE =
          MediaType.parse("application/octet-stream; charset=utf-8");

  public HttpArSeedingService(String arSeedingHost, String payHost, OkHttpClient httpClient) {
    this.arSeedingHost = arSeedingHost;
    this.payHost = payHost;
    this.httpClient = httpClient;
  }

  @Override
  public InputStream sendJsonRequestToArSeeding(
      String path, String request, HashMap<String, String> headers) throws IOException {

    RequestBody requestBody = RequestBody.create(request, JSON_MEDIA_TYPE);

    byte[] res = send(headers, arSeedingHost + path, requestBody);

    return res == null ? null : new ByteArrayInputStream(res);
  }

  @Override
  public InputStream sendBytesRequestToArSeeding(
      String path, byte[] request, HashMap<String, String> headers) throws IOException {

    RequestBody requestBody = RequestBody.create(BYTE_MEDIA_TYPE, request);
    byte[] res = send(headers, arSeedingHost + path, requestBody);
    return new ByteArrayInputStream(res);
  }

  @Override
  public InputStream sendGetRequestToArSeeding(String path,
          HashMap<String, String> headers) throws IOException {
    Headers httpHeaders = Headers.of(headers);
    Request request =
            new Request.Builder()
                    .url(payHost + path).headers(httpHeaders) // Replace with your API
                    // endpoint
                    .build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (response == null || !response.isSuccessful() || response.body() == null) {
        throw new ConnectionException(
                "Failed to retrieve sendPayRequest: " + response.body().string());
      }

      return new ByteArrayInputStream(response.body().bytes());
    }
  }

  @Override
  public String sendPayRequest(String path) throws IOException {
    Request request =
        new Request.Builder()
            .url(payHost + path) // Replace with your API endpoint
            .build();

    try (Response response = httpClient.newCall(request).execute()) {
      if (response == null || !response.isSuccessful() || response.body() == null) {
        throw new ConnectionException(
            "Failed to retrieve sendPayRequest: " + response.body().string());
      }

      return response.body().string();
    }
  }

  private byte[] send(HashMap<String, String> headers, String url, RequestBody requestBody)
          throws IOException {
    Headers httpHeaders = Headers.of(headers);
    okhttp3.Request httpRequest =
            new okhttp3.Request.Builder().url(url).headers(httpHeaders).post(requestBody).build();

    try (okhttp3.Response response = httpClient.newCall(httpRequest).execute()) {
      ResponseBody responseBody = response.body();
      if (response.isSuccessful()) {
        if (responseBody != null) {
          return responseBody.bytes();
        } else {
          return null;
        }
      } else {
        int code = response.code();
        String text = responseBody == null ? "N/A" : responseBody.string();

        throw new ConnectionException("Invalid response received: " + code + "; " + text);
      }
    }
  }
}