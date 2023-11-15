package com.github.permadao.arseedingsdk.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author shiwen.wy
 * @date 2023/10/1 14:15
 */
public interface ArSeedingService {

    InputStream sendJsonRequestToArSeeding(String path, String request,
        HashMap<String, String> headers) throws IOException;

    InputStream sendJsonRequestToEverPay(String path, String request,
            HashMap<String, String> headers) throws IOException;

    InputStream sendBytesRequestToArSeeding(String path, byte[] request,
        HashMap<String, String> headers) throws IOException;

    InputStream sendGetRequestToArSeeding(String path, HashMap<String, String> headers) throws IOException;

    InputStream sendJsonRequestToEverPay(String path, String request,
                                           HashMap<String, String> headers) throws IOException;


    String sendPayRequest(String path) throws IOException;
}
