package com.github.permadao.arseedingsdk.network.impl;

import com.github.permadao.arseedingsdk.network.ArSeedingService;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import com.github.permadao.exception.ConnectionException;

/**
 * @author shiwen.wy
 * @date 2023/10/1 14:16
 */
public class HttpArSeedingService implements ArSeedingService {

  private String arSeedingHost;

  private String payHost;

  private OkHttpClient httpClient;

  public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

  public static final MediaType BYTE_MEDIA_TYPE = MediaType.parse("application/octet-stream; charset=utf-8");

  public HttpArSeedingService(String arSeedingHost, String payHost, OkHttpClient httpClient) {
    this.arSeedingHost = arSeedingHost;
    this.payHost = payHost;
    this.httpClient = httpClient;
  }

  @Override
  public InputStream sendJsonRequestToArSeeding(String pathName, String request, HashMap<String, String> headers) throws IOException {
    RequestBody requestBody = RequestBody.create(request, JSON_MEDIA_TYPE);

    byte[] res = send(headers, arSeedingHost + pathName, requestBody);

    return res == null ? null : new ByteArrayInputStream(res);
  }

  private byte[] send(HashMap<String, String> headers, String url, RequestBody requestBody) throws IOException {
    Headers httpHeaders = Headers.of(headers);
    okhttp3.Request httpRequest = null;
    if(!Objects.isNull(requestBody)){
      httpRequest = new okhttp3.Request.Builder().url(url).headers(httpHeaders).post(requestBody).build();
    }else{
      httpRequest = new okhttp3.Request.Builder().url(url).headers(httpHeaders).build();
    }


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

  @Override
  public InputStream sendBytesRequestToArSeeding(
      String pathName, byte[] request, HashMap<String, String> headers) throws IOException {

    RequestBody requestBody = RequestBody.create(BYTE_MEDIA_TYPE, request);
    byte[] res = send(headers, arSeedingHost + pathName, requestBody);
    return res == null ? null : new ByteArrayInputStream(res);
  }

  @Override
  public InputStream sendGetRequestToArSeeding(String pathName, HashMap<String, String> headers) throws IOException {
    byte[] res = send(headers, arSeedingHost + pathName, null);
    return res == null ? null : new ByteArrayInputStream(res);
  }

}
