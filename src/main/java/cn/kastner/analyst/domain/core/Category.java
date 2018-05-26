package cn.kastner.analyst.domain.core;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

    private String categoryStr;

    @Column(nullable = false)
    private int levelOne;

    @Column(nullable = false)
    private int levelTwo;

    @Column(nullable = false)
    private int levelThree;

    private String levelOneName;

    private String levelTwoName;

    private String levelThreeName;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Item> itemList;
}
