package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.core.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        JpaSpecificationExecutor<ItemRepository> {

    Item findByItemId(Long itemId);
    List<Item> findALlByItemId(Long itemId);
    List<Item> findByZhName(String zhName);
    List<Item> findByZhNameContaining(String zhName);
    Item findBySkuId(String skuId);
    List<Item>  findAllByCategoryCategoryId(Long categoryId);
    List<Item> findAllByBrandBrandIdAndCategoryCategoryId(Long brandId,Long categoryId);

}
