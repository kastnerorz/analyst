package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import javafx.scene.layout.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>,
        JpaSpecificationExecutor<Price> {
    List<Price> findByItem(Item item);
    List<Price> findByItemAndCrawDateTimeAfter(Item item, LocalDateTime crawDateTime);
    Price findByPriceId(Long priceId);
    List<Price> findAllByPriceBetween(Double min, Double max);
    List<Price> findAllByPriceBetweenAndItemItemIdIn(Double min,Double max,List item);
    List<Price> findAllByPriceGreaterThanAndItemItemIdIn(Double min,List item);

}

