package com.github.permadao.arseedingsdk.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * @author shiwen.wy
 * @date 2023/10/1 14:15
 */
public interface ArSeedingService {

    InputStream sendJsonRequestToArSeeding(String request,
        HashMap<String, String> headers) throws IOException;
}
