package cn.kastner.analyst.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long brandId;

    private String brandZhName;

    private String brandEnName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "decimal(5,2)")
    private Float rate;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Item> itemList;

    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<PhoneDetail> phoneDetailList;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandEnName() {
        return brandEnName;
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<PhoneDetail> getPhoneDetailList() {
        return phoneDetailList;
    }

    public void setPhoneDetailList(List<PhoneDetail> phoneDetailList) {
        this.phoneDetailList = phoneDetailList;
    }

    public String getBrandZhName() {
        return brandZhName;
    }

    public void setBrandZhName(String brandZhName) {
        this.brandZhName = brandZhName;
    }
}
