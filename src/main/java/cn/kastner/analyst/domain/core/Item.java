package cn.kastner.analyst.domain.core;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "item")
public class Item {

    @Id
    private Long itemId;

    private String zhName;

    private String enName;

    private String model;

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
    private String advance;

    @Column(columnDefinition = "TEXT")
    private String disAdvance;

    @Column(columnDefinition = "TEXT")
    private String keyFeature;

    private int commentCount;

    private String commentCountStr;
    private LocalDate crawDate;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public String getImageList() {
        return imageList;
    }

    public void setImageList(String imageList) {
        this.imageList = imageList;
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Price> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<Price> priceList) {
        this.priceList = priceList;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getDisAdvance() {
        return disAdvance;
    }

    public void setDisAdvance(String disAdvance) {
        this.disAdvance = disAdvance;
    }

    public String getKeyFeature() {
        return keyFeature;
    }

    public void setKeyFeature(String keyFeature) {
        this.keyFeature = keyFeature;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentCountStr() {
        return commentCountStr;
    }

    public void setCommentCountStr(String commentCountStr) {
        this.commentCountStr = commentCountStr;
    }


    public LocalDate getCrawDate() {
        return crawDate;
    }

    public void setCrawDate(LocalDate crawDate) {
        this.crawDate = crawDate;
    }
}
