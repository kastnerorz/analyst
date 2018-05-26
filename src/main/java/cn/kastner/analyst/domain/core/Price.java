package cn.kastner.analyst.domain.core;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "price")
@Data
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
}
