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

    private String zhName;

    private String enName;

    private String model;

    private String vendorId;

    private String vendor;

    private String commentVersion;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST})
    private Category category;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST})
    private Brand brand;

    @ManyToOne(cascade = {CascadeType.MERGE,
            CascadeType.PERSIST})
    private Market market;

    @Column(columnDefinition = "TEXT")
    private String imageList;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Feature> featureList;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Price> priceList;

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

    private LocalDate crawDate;

    private String color;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSelfSell;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    @Override
    public String getZhName() {
        return zhName;
    }

    @Override
    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    @Override
    public String getEnName() {
        return enName;
    }

    @Override
    public void setEnName(String enName) {
        this.enName = enName;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
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
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public Market getMarket() {
        return market;
    }

    @Override
    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public String getImageList() {
        return imageList;
    }

    @Override
    public void setImageList(String imageList) {
        this.imageList = imageList;
    }

    @Override
    public List<Comment> getCommentList() {
        return commentList;
    }

    @Override
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public List<Price> getPriceList() {
        return priceList;
    }

    @Override
    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    @Override
    public List<Feature> getFeatureList() {
        return featureList;
    }

    @Override
    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
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

    @Override
    public LocalDate getCrawDate() {
        return crawDate;
    }

    @Override
    public void setCrawDate(LocalDate crawDate) {
        this.crawDate = crawDate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getSelfSell() {
        return isSelfSell;
    }

    public void setSelfSell(Boolean selfSell) {
        isSelfSell = selfSell;
    }
}
