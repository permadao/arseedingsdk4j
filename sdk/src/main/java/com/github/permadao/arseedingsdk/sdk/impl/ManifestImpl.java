package com.github.permadao.arseedingsdk.sdk.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.permadao.arseedingsdk.sdk.IManifest;
import com.github.permadao.arseedingsdk.sdk.response.ManifestResponse;
import com.github.permadao.model.bundle.Tag;
import com.github.permadao.model.scheam.ManifestData;
import com.github.permadao.model.scheam.RespOrder;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class ManifestImpl implements IManifest {

    //rootPath string, batchSize int, indexFile string, currency string, noFeeApikey string, needSequence bool
    public ManifestResponse uploadFolder(String rootPath, int batchSize, String indexFile, String currency, String noFeeApikey, boolean needSequence) throws IOException {
        if (StringUtils.isEmpty(indexFile)) {
            indexFile = "index.html";
        }
        ManifestData.ManifestDataBuilder manifestDataBuilder = ManifestData.ManifestDataBuilder.aManifestData();
        ManifestData manifestData = manifestDataBuilder
                .manifest("arweave/paths")
                .version("0.1.0")
                .index(indexFile)
                .paths(new HashMap<>(8)).build();
        List<String> pathFiles = getPathFiles(rootPath);
        CopyOnWriteArrayList<RespOrder> orders = new CopyOnWriteArrayList<>();
        for (String pathFile : pathFiles) {
            Thread t = new Thread(()->{
                try {
                    byte[] bytes = readFileData(pathFile);
                    Tag tag = new Tag();
                    tag.setName("Content-Type");
                    tag.setValue(Files.probeContentType(Paths.get(pathFile)));
                    //sendData
                    RespOrder order = null;
                    orders.add(order);
                    manifestData.getPaths().put(pathFile,new ManifestData.Resource(order.getItemId()));
                } catch (IOException e) {
                    System.out.println("ERROR:" + e.getMessage());
                }
            });
            t.start();
        }
        String manifestStr = new ObjectMapper().writeValueAsString(manifestData);

        return null;
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
}
