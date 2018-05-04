package cn.kastner.analyst.domain;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "price")
public class Price {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String priceId;

    private Item item;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(columnDefinition = "decimal(10,2)")
    private Double price;

    private Market market;

    private Long volume;

    public Price () {
        super();
        priceId = UUID.randomUUID().toString();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
