package cn.kastner.analyst.domain;



import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price")
public class Price {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long priceId;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "item_id")
    private Item item;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Column(columnDefinition = "decimal(10,2)")
    private Double price;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "market_id")
    private Market market;

    private Long volume;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
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
