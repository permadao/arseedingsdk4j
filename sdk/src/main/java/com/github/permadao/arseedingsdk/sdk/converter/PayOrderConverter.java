package com.github.permadao.arseedingsdk.sdk.converter;

import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.arseedingsdk.sdk.response.PayOrdersResponse;

/**
 * @author shiwen.wy
 * @date 2023/10/12 22:54
 */
public class PayOrderConverter {

  public static PayOrder dataSendResponseConvertToPayOrder(DataSendResponse dataSendResponse) {
    if (dataSendResponse == null) {
      return null;
    }
    PayOrder payOrder = new PayOrder();
    payOrder.setItemId(dataSendResponse.getItemId());
    payOrder.setSize(dataSendResponse.getSize());
    payOrder.setBundler(dataSendResponse.getBundler());
    payOrder.setCurrency(dataSendResponse.getCurrency());
    payOrder.setDecimals(dataSendResponse.getDecimals());
    payOrder.setFee(dataSendResponse.getFee());
    payOrder.setPaymentExpiredTime(dataSendResponse.getPaymentExpiredTime());
    payOrder.setExpectedBlock(dataSendResponse.getExpectedBlock());
    return payOrder;
  }

  public static DataSendOrderResponse payOrdersResponseConvertToDataSendOrderResponse(
      PayOrdersResponse payOrdersResponse) {
    if (payOrdersResponse == null) {
      return null;
    }
    DataSendOrderResponse dataSendOrderResponse = new DataSendOrderResponse();
    dataSendOrderResponse.setData(payOrdersResponse.getData());
    dataSendOrderResponse.setAction(payOrdersResponse.getAction());
    dataSendOrderResponse.setAmount(payOrdersResponse.getAmount());
    dataSendOrderResponse.setFrom(payOrdersResponse.getFrom());
    dataSendOrderResponse.setSig(payOrdersResponse.getSig());
    dataSendOrderResponse.setTo(payOrdersResponse.getTo());
    dataSendOrderResponse.setNonce(payOrdersResponse.getNonce());
    dataSendOrderResponse.setChainID(payOrdersResponse.getChainID());
    dataSendOrderResponse.setFee(payOrdersResponse.getFee());
    dataSendOrderResponse.setChainType(payOrdersResponse.getChainType());
    dataSendOrderResponse.setFeeRecipient(payOrdersResponse.getFeeRecipient());
    dataSendOrderResponse.setTokenID(payOrdersResponse.getTokenID());
    dataSendOrderResponse.setTokenSymbol(payOrdersResponse.getTokenSymbol());
    dataSendOrderResponse.setVersion(payOrdersResponse.getVersion());
    return dataSendOrderResponse;
  }
}
