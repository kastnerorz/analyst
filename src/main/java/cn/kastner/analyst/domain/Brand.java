package cn.kastner.analyst.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    private String brandId;

    private String brandName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "decimal(2,2)")
    private Float rate;

    public Brand () {
        brandId = UUID.randomUUID().toString();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
