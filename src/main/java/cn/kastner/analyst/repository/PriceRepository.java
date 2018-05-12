package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long>,
        JpaSpecificationExecutor<Price> {
    List<Price> findPriceByItem(Item item);

    Price findByPriceId(Long priceId);
}
