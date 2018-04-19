package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, String> {
    List<Price> findAllByOrderByDateAsc ();
}
