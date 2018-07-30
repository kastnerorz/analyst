package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        JpaSpecificationExecutor<ItemRepository> {

    Item findItemById(Long itemId);
    List<Item> findByZhName(String zhName);
    List<Item> findByZhNameContaining(String zhName);
    List<Item> findByCategory(Category category);
    List<Item>  findAllByCategoryCategoryId(Long categoryId);
    List<Item> findAllByBrandBrandIdAndCategoryCategoryId(Long brandId,Long categoryId);

}
