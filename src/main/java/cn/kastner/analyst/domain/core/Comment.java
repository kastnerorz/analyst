package cn.kastner.analyst.domain.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "comment")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
    private Feature feature;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE})
    @JsonIgnore
    private Item item;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition="BOOLEAN DEFAULT true")
    private Boolean isGood;

    private LocalDate crawDate;
}
