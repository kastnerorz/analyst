package cn.kastner.analyst.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.soap.Text;
import java.util.UUID;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    private String id;
    private String name;
    private Text introduction;
    private Integer rate;

    public Brand () {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Text getIntroduction() {
        return introduction;
    }

    public void setIntroduction(Text introduction) {
        this.introduction = introduction;
    }
}
