package cn.kastner.analyst.domain.core;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "item")
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long itemId;

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

    private LocalDate crawDate;

    private String color;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isSelfSell;
}
