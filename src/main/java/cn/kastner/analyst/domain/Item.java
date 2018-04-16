package cn.kastner.analyst.domain;


import sun.plugin.dom.core.Text;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "item")
public class Item {
    @Id
    private String itemId;

    private String cname;

    private String ename;

    private String brandId;

    @Column(columnDefinition = "TEXT")
    private String advan;

    @Column(columnDefinition = "TEXT")
    private String disAdvan;

    public Item () {
        itemId = UUID.randomUUID().toString();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getAdvan() {
        return advan;
    }

    public void setAdvan(String advan) {
        this.advan = advan;
    }

    public String getDisAdvan() {
        return disAdvan;
    }

    public void setDisAdvan(String disAdvan) {
        this.disAdvan = disAdvan;
    }
}
