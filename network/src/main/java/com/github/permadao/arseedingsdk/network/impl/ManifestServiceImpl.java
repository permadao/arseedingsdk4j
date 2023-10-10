package com.github.permadao.arseedingsdk.network.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.network.ManifestService;
import com.github.permadao.model.scheam.RespOrder;
import com.github.permadao.model.tx.Transaction;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManifestServiceImpl implements ManifestService {

    public List<Transaction> batchPayOrders(List<RespOrder> orders) throws JsonProcessingException {
        if (orders.size() <= 500){;
            return payOrders(orders);
        }
        //拆分
        int startIndex = 0;
        int batchSize = 500;
        List<Transaction> everTxs = Lists.newArrayList();
        while (startIndex < orders.size()) {
            int endIndex = startIndex + batchSize;
            if (endIndex > orders.size()) {
                endIndex = orders.size();
            }
            List<RespOrder> temp = orders.subList(startIndex, endIndex);
            everTxs.addAll(payOrders(temp));
            startIndex += batchSize;
        }
        return everTxs;
    }

    private List<Transaction> payOrders(List<RespOrder> orders) throws JsonProcessingException {
        if (orders.isEmpty()){
            throw new RuntimeException("order is null");
        }
        if (StringUtils.isEmpty(orders.get(0).getFee())){
            return Lists.newArrayList();
        }
        if (orders.size() > 500){
            throw new RuntimeException("please use BatchPayOrders function");
        }
        List<RespOrder> checkOrders = orders.stream().filter(order -> order.getBundler().equals(orders.get(0).getBundler()) && order.getCurrency().equals(orders.get(0).getCurrency())).collect(Collectors.toList());
        if (checkOrders.size() != orders.size()){
            throw new RuntimeException("orders bundler and currency must be equal");
        }
        BigDecimal totalFee = BigDecimal.ZERO;
        List<String> itemIds = Lists.newArrayList();
        for (RespOrder order : orders) {
            totalFee = totalFee.add(new BigDecimal(order.getFee()));
            itemIds.add(order.getItemId());
        }
        Map<String,Object>  payTxDataMap = new HashMap<>();
        payTxDataMap.put("appName","arseeding");
        payTxDataMap.put("action","payment");
        payTxDataMap.put("itemIds",itemIds);
        byte[] payTxData = new ObjectMapper().writeValueAsBytes(payTxDataMap);
        //TODO tokenTags := s.Pay.SymbolToTagArr(orders[0].Currency)
        List<String> tokenTags = null;
        if (tokenTags.isEmpty()){
            throw new RuntimeException("currency not exist token");
        }
        //TODO tokBals, err := s.Pay.Cli.Balances(s.Pay.AccId)
        List tokBals = null;
        //TODO tagToBal := make(map[string]*big.Int)
        //	for _, bal := range tokBals.Balances {
        //		amt, _ := new(big.Int).SetString(bal.Amount, 10)
        //		tagToBal[bal.Tag] = amt
        //	}
        List tagToBal = null;
        String useTag = "";
        if (StringUtils.isEmpty(useTag)){
            throw new RuntimeException("token balance insufficient");
        }

        //TODO everTx, err = s.Pay.Transfer(useTag, totalFee, orders[0].Bundler, string(payTxData))
        Transaction everTx = new Transaction();
        return Lists.newArrayList(everTx);
    }
}
