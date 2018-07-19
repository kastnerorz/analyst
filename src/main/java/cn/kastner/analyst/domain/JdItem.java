package cn.kastner.analyst.domain;

import javax.persistence.*;

@Entity
@Table(name = "jd_item")
public class JdItem extends Item{

    @Column(unique = true)
    private String skuId;

    private String vendorId;

    private String vendor;

    private String commentVersion;

    @Column(columnDefinition = "TEXT")
    private String keyFeature;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCommentVersion() {
        return commentVersion;
    }

    public void setCommentVersion(String commentVersion) {
        this.commentVersion = commentVersion;
    }

    @Override
    public String getKeyFeature() {
        return keyFeature;
    }

    @Override
    public void setKeyFeature(String keyFeature) {
        this.keyFeature = keyFeature;
    }


}
