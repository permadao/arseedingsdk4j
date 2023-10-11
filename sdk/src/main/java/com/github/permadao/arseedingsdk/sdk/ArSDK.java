package com.github.permadao.arseedingsdk.sdk;

import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.model.bundle.Tag;

import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/1 21:53
 */
public interface ArSDK {

  DataSendOrderResponse sendDataAndPay(
      byte[] data,
      String currency,
      List<Tag> tags,
      String target,
      String anchor,
      boolean needSequence) throws Exception;
}
