package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        JpaSpecificationExecutor<ItemRepository> {

    Item findByItemId(Long itemId);
    List<Item> findALlByItemId(Long itemId);
    List<Item> findByZhName(String zhName);
    List<Item> findAllByZhName(String zhName);
    Item findBySkuId(String skuId);
}
