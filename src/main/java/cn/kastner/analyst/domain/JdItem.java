package cn.kastner.analyst.domain;

import javax.persistence.*;

@Entity
@Table(name = "jd_item")
public class JdItem extends Item{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skuId;

    private String vendorId;

    private String vendor;

    private String commentVersion;

    @Column(columnDefinition = "TEXT")
    private String keyFeature;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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
