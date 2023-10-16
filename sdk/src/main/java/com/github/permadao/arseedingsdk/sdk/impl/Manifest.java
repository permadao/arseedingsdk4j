package com.github.permadao.arseedingsdk.sdk.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.sdk.IManifest;
import com.github.permadao.arseedingsdk.sdk.converter.PayOrderConverter;
import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.model.PayTransaction;
import com.github.permadao.arseedingsdk.sdk.request.PayOrdersRequest;
import com.github.permadao.arseedingsdk.sdk.response.DataSendOrderResponse;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.arseedingsdk.sdk.response.ManifestResponse;
import com.github.permadao.arseedingsdk.sdk.response.PayOrdersResponse;
import com.github.permadao.model.bundle.Tag;
import com.github.permadao.model.scheam.ManifestData;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Manifest implements IManifest {
    private ArHttpSDK arHttpSDK;
    private PayOrdersRequest payOrdersRequest;

    public Manifest(ArHttpSDK arHttpSDK, PayOrdersRequest payOrdersRequest) {
        this.arHttpSDK = arHttpSDK;
        this.payOrdersRequest = payOrdersRequest;
    }

    public ManifestResponse.UploadResponse uploadFolder(String rootPath, int batchSize, String indexFile, String currency) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, currency, false);
    }

    public ManifestResponse.UploadFolderAndPayResponse uploadFolderAndPay(String rootPath, int batchSize, String indexFile, String currency) throws Exception {
        ManifestResponse.UploadResponse uploadResponse = uploadFolder(rootPath, batchSize, indexFile, currency, false);
        List<PayTransaction> everTxs = batchPayOrders(uploadResponse.getOrders());
        return new ManifestResponse.UploadFolderAndPayResponse(uploadResponse, everTxs);
    }

    public ManifestResponse.UploadResponse uploadFolderWithNoFee(String rootPath, int batchSize, String indexFile) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, "", false);
    }


    public ManifestResponse.UploadResponse uploadFolderWithSequence(String rootPath, int batchSize, String indexFile) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, "", true);
    }


    public ManifestResponse.UploadResponse uploadFolder(String rootPath, int batchSize, String indexFile, String currency, boolean needSequence) throws Exception {
        if (StringUtils.isEmpty(indexFile)) {
            indexFile = "index.html";
        }
        ManifestData.ManifestDataBuilder manifestDataBuilder = ManifestData.ManifestDataBuilder.aManifestData();
        ManifestData manifestData = manifestDataBuilder
                .manifest("arweave/paths")
                .version("0.1.0")
                .index(indexFile)
                .paths(new ConcurrentHashMap<>(8)).build();
        List<String> pathFiles = getPathFiles(rootPath);
        CopyOnWriteArrayList<PayOrder> orders = new CopyOnWriteArrayList<>();
        if (batchSize == 0) {
            batchSize = 10;
        }
        ExecutorService threadPool = Executors.newFixedThreadPool(batchSize);
        CountDownLatch countDownLatch = new CountDownLatch(pathFiles.size());
        for (String pathFile : pathFiles) {
            threadPool.submit(() -> {
                try {
                    byte[] bytes = readFileData(pathFile);
                    Tag tag = new Tag();
                    tag.setName("Content-Type");
                    tag.setValue(Files.probeContentType(Paths.get(pathFile)));
                    DataSendResponse
                        order = arHttpSDK.sendData(bytes, currency, Lists.newArrayList(tag), "", "", needSequence);
                    orders.add(PayOrderConverter.dataSendResponseConvertToPayOrder(order));
                    manifestData.getPaths().put(pathFile, new ManifestData.Resource(order.getItemId()));
                    countDownLatch.countDown();
                } catch (Exception e) {
                    System.out.println(String.format("ERROR:An exception occurred during uploading file [%s],the exception's details are  ", pathFile) + e);
                }
            });
        }
        countDownLatch.await();
        byte[] manifestBytes = new ObjectMapper().writeValueAsBytes(manifestData);
        Tag tag = new Tag();
        Tag tag2 = new Tag();
        tag.setName("Type");
        tag.setValue("manifest");
        tag2.setName("Content-Type");
        tag2.setValue("application/x.arweave-manifest+json");

        DataSendResponse order = arHttpSDK.sendData(manifestBytes, currency, Lists.newArrayList(tag,tag2), "", "", needSequence);;
        orders.add(PayOrderConverter.dataSendResponseConvertToPayOrder(order));

        return new ManifestResponse.UploadResponse(orders, order.getItemId());
    }

    public List<String> getPathFiles(String rootPath) throws IOException {
        List<String> pathFiles = new ArrayList<>(16);
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(f -> {
                        pathFiles.add(f.getAbsolutePath());
                    });
            return pathFiles;
        } catch (IOException e) {
            System.out.println("ERROR:" + e.getMessage());
            throw e;
        }
    }

    public byte[] readFileData(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }


    public List<PayTransaction> batchPayOrders(List<PayOrder> orders) throws Exception {
        if (orders.size() <= 500) {
            return Lists.newArrayList(transform(payOrdersRequest.send(orders)));
        }
        //拆分
        int startIndex = 0;
        int batchSize = 500;
        List<PayTransaction> everTxs = Lists.newArrayList();
        while (startIndex < orders.size()) {
            int endIndex = startIndex + batchSize;
            if (endIndex > orders.size()) {
                endIndex = orders.size();
            }
            List<PayOrder> temp = orders.subList(startIndex, endIndex);
            everTxs.add(transform(payOrdersRequest.send(temp)));
            startIndex += batchSize;
        }
        return everTxs;
    }

    public PayTransaction transform(PayOrdersResponse payOrdersResponse) {
        PayTransaction payTransaction = new PayTransaction();
        payTransaction.setTokenSymbol(payOrdersResponse.getTokenSymbol());
        payTransaction.setAction(payOrdersResponse.getAction());
        payTransaction.setFrom(payOrdersResponse.getFrom());
        payTransaction.setTo(payOrdersResponse.getTo());
        payTransaction.setAmount(payOrdersResponse.getAmount());
        payTransaction.setFee(payOrdersResponse.getFee());
        payTransaction.setFeeRecipient(payOrdersResponse.getFeeRecipient());
        payTransaction.setNonce(payOrdersResponse.getNonce());
        payTransaction.setTokenID(payOrdersResponse.getTokenID());
        payTransaction.setChainType(payOrdersResponse.getChainType());
        payTransaction.setChainID(payOrdersResponse.getChainID());
        payTransaction.setData(payOrdersResponse.getData());
        payTransaction.setVersion(payOrdersResponse.getVersion());
        payTransaction.setSig(payOrdersResponse.getSig());
        return payTransaction;
    }

}
