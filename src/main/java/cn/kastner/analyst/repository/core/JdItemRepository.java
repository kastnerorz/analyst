package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.JdItem;
import cn.kastner.analyst.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JdItemRepository extends JpaRepository<JdItem, Long>,
        JpaSpecificationExecutor<JdItemRepository> {
    JdItem findJdItemById(Long id);
    JdItem findBySkuId(String skuId);
}
