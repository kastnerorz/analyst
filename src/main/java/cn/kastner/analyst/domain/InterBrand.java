package cn.kastner.analyst.domain;

import javax.persistence.Entity;

@Entity
public class InterBrand {

    private String itemId;
    private String brandId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
