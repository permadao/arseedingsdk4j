package com.github.permadao.arseedingsdk.sdk.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.sdk.converter.PayOrderConverter;
import com.github.permadao.arseedingsdk.sdk.impl.ArHttpSDK;
import com.github.permadao.arseedingsdk.sdk.model.PayOrder;
import com.github.permadao.arseedingsdk.sdk.model.PayTransaction;
import com.github.permadao.arseedingsdk.sdk.model.exception.BizException;
import com.github.permadao.arseedingsdk.sdk.response.DataSendResponse;
import com.github.permadao.arseedingsdk.sdk.response.ManifestUploadResponse;
import com.github.permadao.arseedingsdk.sdk.response.UploadFolderAndPayResponse;
import com.github.permadao.model.bundle.Tag;
import com.github.permadao.model.scheam.ManifestData;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @author shiwen.wy
 * @date 2023/10/16 16:26
 */
public class ManifestRequest {
    private static final Logger log = LoggerFactory.getLogger(ManifestRequest.class);
    private static final String DEFAULT_INDEX_FILE = "index.html";
    private ArHttpSDK arHttpSDK;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ManifestRequest(ArHttpSDK arHttpSDK) {
        this.arHttpSDK = arHttpSDK;
    }

    public ManifestUploadResponse uploadFolder(
            String rootPath, int batchSize, String indexFile, String currency) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, currency, false);
    }

    public ManifestUploadResponse uploadFolderWithNoFee(
            String rootPath, int batchSize, String indexFile) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, "", false);
    }

    public ManifestUploadResponse uploadFolderWithSequence(
            String rootPath, int batchSize, String indexFile) throws Exception {
        return uploadFolder(rootPath, batchSize, indexFile, "", true);
    }

    public UploadFolderAndPayResponse uploadFolderAndPay(
            String rootPath,
            int batchSize,
            String indexFile,
            String currency,
            PayOrdersRequest payOrdersRequest)
            throws Exception {
        ManifestUploadResponse uploadResponse =
                uploadFolder(rootPath, batchSize, indexFile, currency, false);
        List<PayTransaction> everTxs = batchPayOrders(uploadResponse.getOrders(), payOrdersRequest);
        return new UploadFolderAndPayResponse(uploadResponse, everTxs);
    }

    public ManifestUploadResponse uploadFolder(
            String rootPath, int batchSize, String indexFile, String currency, boolean needSequence)
            throws Exception {
        if (StringUtils.isEmpty(indexFile)) {
            indexFile = DEFAULT_INDEX_FILE;
        }

        ManifestData manifestData = buildManifestData(indexFile);

        List<String> pathFiles = getPathFiles(rootPath);

        CopyOnWriteArrayList<PayOrder> orders = new CopyOnWriteArrayList<>();

        runDataSendThread(pathFiles, currency, needSequence, manifestData, orders, pathFiles.size(), batchSize);

        byte[] manifestBytes = objectMapper.writeValueAsBytes(manifestData);

        DataSendResponse order =
                arHttpSDK.sendData(manifestBytes, currency, buildManifestTags(), "", "", needSequence);

        orders.add(PayOrderConverter.dataSendResponseConvertToPayOrder(order));
        return new ManifestUploadResponse(orders, order.getItemId());
    }

    private List<PayTransaction> batchPayOrders(
            List<PayOrder> orders, PayOrdersRequest payOrdersRequest) throws Exception {
        if (orders.size() <= 500) {
            return Lists.newArrayList(
                    PayOrderConverter.payOrdersResponseConvertToPayTransaction(
                            payOrdersRequest.send(orders)));
        }
        // 拆分
        int startIndex = 0;
        int batchSize = 500;
        List<PayTransaction> everTxs = Lists.newArrayList();
        while (startIndex < orders.size()) {
            int endIndex = startIndex + batchSize;
            if (endIndex > orders.size()) {
                endIndex = orders.size();
            }
            List<PayOrder> temp = orders.subList(startIndex, endIndex);
            everTxs.add(
                    PayOrderConverter.payOrdersResponseConvertToPayTransaction(payOrdersRequest.send(temp)));
            startIndex += batchSize;
        }
        return everTxs;
    }

    private void runDataSendThread(
            List<String> pathFiles,
            String currency,
            boolean needSequence,
            ManifestData manifestData,
            CopyOnWriteArrayList<PayOrder> orders,
            int pathFilesSize,
            int batchSize) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(batchSize);
        CountDownLatch countDownLatch = new CountDownLatch(pathFilesSize);
        for (String pathFile : pathFiles) {
            try {
                threadPool.execute(
                        () -> {
                            try {
                                byte[] bytes = readFileData(pathFile);
                                Tag tag = new Tag();
                                tag.setName("Content-Type");
                                tag.setValue(Files.probeContentType(Paths.get(pathFile)));
                                DataSendResponse order =
                                        arHttpSDK.sendData(
                                                bytes, currency, Lists.newArrayList(tag), "", "", needSequence);
                                orders.add(PayOrderConverter.dataSendResponseConvertToPayOrder(order));
                                manifestData.getPaths().put(pathFile, new ManifestData.Resource(order.getItemId()));
                            } catch (Exception e) {
                                log.error("ERROR: ", e);
                                throw new BizException(e.getMessage());
                            } finally {
                                countDownLatch.countDown();
                            }
                        });
            } catch (Throwable e) {
                log.error(
                        String.format(
                                "ERROR:An exception occurred during uploading file [%s],the exception's details are  ",
                                pathFile),
                        e);
                threadPool.shutdownNow();
                throw e;
            }
        }
        if (!countDownLatch.await(10, TimeUnit.MINUTES)) {
            threadPool.shutdownNow();
            throw new BizException("Upload Timeout!");
        }
        threadPool.shutdown();
    }

    private List<Tag> buildManifestTags() {
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tag1.setName("Type");
        tag1.setValue("manifest");
        tag2.setName("Content-Type");
        tag2.setValue("application/x.arweave-manifest+json");
        return Lists.newArrayList(tag1, tag2);
    }

    private ManifestData buildManifestData(String indexFile) {
        ManifestData.ManifestDataBuilder manifestDataBuilder =
                ManifestData.ManifestDataBuilder.aManifestData();
        ManifestData manifestData =
                manifestDataBuilder
                        .manifest("arweave/paths")
                        .version("0.1.0")
                        .index(indexFile)
                        .paths(new ConcurrentHashMap<>(8))
                        .build();
        return manifestData;
    }

    private List<String> getPathFiles(String rootPath) throws IOException {
        List<String> pathFiles = new ArrayList<>(16);
        try (Stream<Path> paths = Files.walk(Paths.get(rootPath))) {
            paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(
                            f -> {
                                pathFiles.add(f.getAbsolutePath());
                            });
            return pathFiles;
        } catch (IOException e) {
            log.info("manifest get path files error", e);
            throw e;
        }
    }

    private byte[] readFileData(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}
