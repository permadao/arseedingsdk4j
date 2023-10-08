package com.github.permadao.model;

import com.github.permadao.model.bundle.Tag;

import java.util.List;

//type Transaction struct {
//	Format     int      `json:"format"`
//	ID         string   `json:"id"`
//	LastTx     string   `json:"last_tx"`
//	Owner      string   `json:"owner"` // utils.Base64Encode(wallet.PubKey.N.Bytes())
//	Tags       []Tag    `json:"tags"`
//	Target     string   `json:"target"`
//	Quantity   string   `json:"quantity"`
//	Data       string   `json:"data"` // base64.encode
//	DataReader *os.File `json:"-"`    // when dataSize too big use dataReader, set Data = ""
//	DataSize   string   `json:"data_size"`
//	DataRoot   string   `json:"data_root"`
//	Reward     string   `json:"reward"`
//	Signature  string   `json:"signature"`
//
//	// Computed when needed.
//	Chunks *Chunks `json:"-"`
//}
public class Transaction {
    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
