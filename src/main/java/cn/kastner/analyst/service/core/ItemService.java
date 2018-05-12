package cn.kastner.analyst.service.core;

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


}
