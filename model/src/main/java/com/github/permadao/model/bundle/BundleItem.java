package com.github.permadao.model.bundle;

import java.io.Serializable;
import java.util.List;

/**
 * @author shiwen.wy
 * @date 2023/10/1 15:24
 */
public class BundleItem implements Serializable {
  private static final long serialVersionUID = 4185856598225890579L;
  private int signatureType;
  private String signature;
  private String owner;
  private String target;
  private String anchor;
  private List<Tag> tags;
  private String data;
  private String id;
  private String tagsBy;

  public static class Builder {
    private int signatureType;
    private String signature;
    private String owner;
    private String target;
    private String anchor;
    private List<Tag> tags;
    private String data;
    private String id;
    private String tagsBy;

    public Builder signatureType(int signatureType) {
      this.signatureType = signatureType;
      return this;
    }

    public Builder signature(String signature) {
      this.signature = signature;
      return this;
    }

    public Builder owner(String owner) {
      this.owner = owner;
      return this;
    }

    public Builder target(String target) {
      this.target = target;
      return this;
    }

    public Builder anchor(String anchor) {
      this.anchor = anchor;
      return this;
    }

    public Builder tags(List<Tag> tags) {
      this.tags = tags;
      return this;
    }

    public Builder data(String data) {
      this.data = data;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder tagsBy(String tagsBy) {
      this.tagsBy = tagsBy;
      return this;
    }

    public BundleItem build() {
      BundleItem bundleItem = new BundleItem();
      bundleItem.signatureType = this.signatureType;
      bundleItem.signature = this.signature;
      bundleItem.owner = this.owner;
      bundleItem.target = this.target;
      bundleItem.anchor = this.anchor;
      bundleItem.tags = this.tags;
      bundleItem.data = this.data;
      bundleItem.id = this.id;
      bundleItem.tagsBy = this.tagsBy;
      return bundleItem;
    }
  }

  public int getSignatureType() {
    return signatureType;
  }

  public void setSignatureType(int signatureType) {
    this.signatureType = signatureType;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getAnchor() {
    return anchor;
  }

  public void setAnchor(String anchor) {
    this.anchor = anchor;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTagsBy() {
    return tagsBy;
  }

  public void setTagsBy(String tagsBy) {
    this.tagsBy = tagsBy;
  }

  @Override
  public String toString() {
    return "BundleItem{"
        + "signatureType="
        + signatureType
        + ", signature='"
        + signature
        + '\''
        + ", owner='"
        + owner
        + '\''
        + ", target='"
        + target
        + '\''
        + ", anchor='"
        + anchor
        + '\''
        + ", tags="
        + tags
        + ", data='"
        + data
        + '\''
        + ", id='"
        + id
        + '\''
        + ", tagsBy='"
        + tagsBy
        + '\''
        + '}';
  }
}
