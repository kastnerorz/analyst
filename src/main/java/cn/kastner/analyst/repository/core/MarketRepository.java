package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MarketRepository extends JpaRepository<Market, Long>,
        JpaSpecificationExecutor<MarketRepository> {
    Market findByCode(Market.Code code);
}
