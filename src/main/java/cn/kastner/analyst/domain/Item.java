package cn.kastner.analyst.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "item")
public class Item {
    @Id
    private String id;
    private String cname;
    private String ename;
    private String brandId;
    private String advan;
    private String disAdvan;

    public Item () {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
