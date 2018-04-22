package cn.kastner.analyst.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "market")
public class Market {

    @Id
    private String marketId;

    private String marketName;

    private String url;

    public Market (String marketName) {
        marketId = UUID.randomUUID().toString();
        if ("京东".equals(marketName)) {
            url = "item.jd.com";
        } else {
            url = "";
        }
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
