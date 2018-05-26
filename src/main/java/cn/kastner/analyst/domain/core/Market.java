package cn.kastner.analyst.domain.core;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "market")
@Data
public class Market {


    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public enum Code {
        JD,
        TB,
        TMALL,
        DD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long marketId;

    private String marketName;

    private String url;

    private Code code;

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL)
    private List<Item> itemList;

    public Market (Code code) {
        if (Code.JD == code) {
            marketName = "京东";
            url = "item.jd.com";
        } else if (Code.TB == code) {
            marketName = "淘宝";
            url = "item.taobao.com";
        } else if (Code.TMALL == code) {
            marketName = "天猫";
            url = "detail.tmall.com";
        }
    }

    public Market () {

    }
}
