package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
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
}
