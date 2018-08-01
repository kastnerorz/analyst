package cn.kastner.analyst.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String zhName;

    private String enName;

    private String model;

    private String description;

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
    private String imageGroup;

    @JsonIgnore
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Feature> featureList;

    @JsonIgnore
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

    private int generalCount;

    private String generalCountStr;

    private int goodCount;

    private String goodCountStr;

    private int poorCount;

    private String poorCountStr;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSelfSell;

    private LocalDate crawDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
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


    public LocalDate getCrawDate() {
        return crawDate;
    }

    public void setCrawDate(LocalDate crawDate) {
        this.crawDate = crawDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
