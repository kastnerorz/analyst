package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Item;

import java.util.List;

public interface ItemService {

    /**
     * create a item
     *
     * @param item
     * @return item inserted
     */
    Item insertByItem(Item item);

    /**
     * retrieve a item by id
     *
     * @param itemId
     * @return item retrieved
     */
    Item findById(Long itemId);

    /**
     * @param skuId
     * @return
     */
    Item findBySkuId(String skuId);

    /**
     * 精确查找某商品
     * @param zhName
     * @return
     */
    List<Item> findByZhName(String zhName);

    /**
     * 模糊查找某商品
     * @param zhName 中文名
     * @return 商品列表
     */
    List<Item> findByZhNameLike(String zhName);
    /**
     * retrieve all categories
     *
     * @return
     */
    List<Item> findAll();

    /**
     * update a item by id
     *
     * @param item
     * @return
     */
    Item update(Item item);

    /**
     * delete a item by id
     *
     * @param itemId
     * @return item deleted
     */
    Item delete(Long itemId);

    List<Item> findByCategory(Category category);

    List<Item> findByCategoryId(Long categoryId);

    List<Item> findByBrandIdAndCategoryId(Long brandId,Long categoryId);
}
