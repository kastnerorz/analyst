package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>,
        JpaSpecificationExecutor<Price> {
    List<Price> findByItem(Item item);
    List<Price> findByItemAndCrawDateTimeAfter(Item item, LocalDateTime crawDateTime);
    Price findByPriceId(Long priceId);
    List<Price> findByItemOrderByCrawDateTimeDesc(Item item);
    List<Price> findAllByPriceBetween(Double min, Double max);
    List<Price> findAllByPriceBetweenAndItemIdIn(Double min, Double max, List<Long> item);
    List<Price> findAllByPriceGreaterThanAndItemIdIn(Double min, List<Long> item);

}

