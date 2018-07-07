package cn.kastner.analyst.domain.crawler;

import cn.kastner.analyst.domain.core.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    private int commentCount;

    private String commentCountStr;

    private int generalCount;

    private String generalCountStr;

    private int goodCount;

    private String goodCountStr;

    private int poorCount;

    private String poorCountStr;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSelfSell;

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

    @Override
    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String getCommentCountStr() {
        return commentCountStr;
    }

    @Override
    public void setCommentCountStr(String commentCountStr) {
        this.commentCountStr = commentCountStr;
    }

    public int getGeneralCount() {
        return generalCount;
    }

    public void setGeneralCount(int generalCount) {
        this.generalCount = generalCount;
    }

    public String getGeneralCountStr() {
        return generalCountStr;
    }

    public void setGeneralCountStr(String generalCountStr) {
        this.generalCountStr = generalCountStr;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodCountStr() {
        return goodCountStr;
    }

    public void setGoodCountStr(String goodCountStr) {
        this.goodCountStr = goodCountStr;
    }

    public int getPoorCount() {
        return poorCount;
    }

    public void setPoorCount(int poorCount) {
        this.poorCount = poorCount;
    }

    public String getPoorCountStr() {
        return poorCountStr;
    }

    public void setPoorCountStr(String poorCountStr) {
        this.poorCountStr = poorCountStr;
    }

    public Boolean getSelfSell() {
        return isSelfSell;
    }

    public void setSelfSell(Boolean selfSell) {
        isSelfSell = selfSell;
    }
}
