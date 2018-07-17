package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.JdItem;

import java.util.List;

public interface JdItemService {
    /**
     * create a jdItem
     *
     * @param jdItem
     * @return jdItem inserted
     */
    JdItem insertByJdItem(JdItem jdItem);

    /**
     * retrieve a jdItem by id
     *
     * @param jdItemId
     * @return jdItem retrieved
     */
    JdItem findById(Long jdItemId);

    /**
     * @param skuId
     * @return
     */
    JdItem findBySkuId(String skuId);


    /**
     * retrieve all categories
     *
     * @return
     */
    List<JdItem> findAll();

    /**
     * update a jdItem by jdItem
     *
     * @param jdItem
     * @return
     */
    JdItem update(JdItem jdItem);

    /**
     * delete a jdItem by jdItem
     *
     * @param jdItemId
     * @return jdItem deleted
     */
    JdItem delete(Long jdItemId);
}
