package cn.kastner.analyst.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "interMarket")
public class InterMarket {

    private String itemId;
    private String marketId;


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
