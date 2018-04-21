package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, String> {
    List<Price> findPriceByItemId(String itemId);
}
