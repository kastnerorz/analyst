package cn.kastner.analyst.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String brandId;

    private String brandZhName;

    private String brandEnName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "decimal(5,2)")
    private Float rate;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandZhName() {
        return brandZhName;
    }

    public void setBrandZhName(String brandZhName) {
        this.brandZhName = brandZhName;
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

    public String getBrandEnName() {
        return brandEnName;
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }
}
