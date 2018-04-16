package cn.kastner.analyst.domain;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "price")
public class Price {

    @Id
    private String itemId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String price;

    private String marketId;

    private Long volume;

    public Price () {
        itemId = UUID.randomUUID().toString();
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }
}
