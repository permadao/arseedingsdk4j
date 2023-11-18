package com.github.permadao.model.scheam;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


public class ManifestData {
    private String manifest;
    private String version;
    private IndexPath index;
    private Map<String, Resource> paths;

    static class IndexPath {
        private String path;

        public IndexPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static class Resource {
        @JsonProperty("id")
        private String TxId;

        public Resource(String txId) {
            this.TxId = txId;
        }

        public String getTxId() {
            return TxId;
        }

        public void setTxId(String txId) {
            TxId = txId;
        }
    }

    public String getManifest() {
        return manifest;
    }

    public void setManifest(String manifest) {
        this.manifest = manifest;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public IndexPath getIndex() {
        return index;
    }

    public void setIndex(IndexPath index) {
        this.index = index;
    }

    public Map<String, Resource> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Resource> paths) {
        this.paths = paths;
    }


    public static final class ManifestDataBuilder {
        private String manifest;
        private String version;
        private IndexPath index;
        private Map<String, Resource> paths;

        private ManifestDataBuilder() {
        }

        public static ManifestDataBuilder aManifestData() {
            return new ManifestDataBuilder();
        }

        public ManifestDataBuilder manifest(String manifest) {
            this.manifest = manifest;
            return this;
        }

        public ManifestDataBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ManifestDataBuilder index(String index) {
            this.index = new IndexPath(index);
            return this;
        }

        public ManifestDataBuilder paths(Map<String, Resource> paths) {
            this.paths = paths;
            return this;
        }

        public ManifestData build() {
            ManifestData manifestData = new ManifestData();
            manifestData.setManifest(manifest);
            manifestData.setVersion(version);
            manifestData.setIndex(index);
            manifestData.setPaths(paths);
            return manifestData;
        }
    }
}
