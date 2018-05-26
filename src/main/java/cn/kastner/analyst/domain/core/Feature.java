package cn.kastner.analyst.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "feature")
@Data
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long featureId;

    private String component;

    private String content;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JsonIgnore
    private Item item;

}
