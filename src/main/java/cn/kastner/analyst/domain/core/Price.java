package cn.kastner.analyst.domain.core;



import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Item item;

    private LocalDateTime crawDateTime;

    @Column(columnDefinition = "decimal(10,2)")
    private Double price;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    private Market market;

    private Long volume;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getCrawDateTime() {
        return crawDateTime;
    }

    public void setCrawDateTime(LocalDateTime crawDateTime) {
        this.crawDateTime = crawDateTime;
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
