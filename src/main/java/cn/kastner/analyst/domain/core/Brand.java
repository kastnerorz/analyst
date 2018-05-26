package cn.kastner.analyst.domain.core;

import cn.kastner.analyst.domain.detail.PhoneDetail;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "brand")
@Data
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long brandId;

    private String brandZhName;

    private String brandEnName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(columnDefinition = "decimal(5,2)")
    private Float rate;

    @OneToMany(mappedBy = "brand")
    private List<Item> itemList;

    @OneToMany
    private List<PhoneDetail> phoneDetailList;
}
