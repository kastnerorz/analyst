package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceService {

    /**
     * create a price
     *
     * @param price
     * @return price inserted
     */
    Price insertByPrice(Price price);

    /**
     * retrieve a price by id
     *
     * @param priceId
     * @return price retrieved
     */
    Price findById(Long priceId);

    /**
     * retrieve all prices by Item
     *
     * @return
     */
    List<Price> findByItemAndCrawDateTimeAfter(Item item, LocalDateTime crawDateTime);

    /**
     * 根据商品查找最新价格
     * @param item 商品
     * @return 价格
     */
    Price findLatestByItem(Item item);

    /**
     * 根据商品查找所有价格
     * @param item
     * @return
     */
    List<Price> findByItem(Item item);

    /**
     * update a price by id
     *
     * @param price
     * @return
     */
    Price update(Price price);

    /**
     * delete a price by id
     *
     * @param priceId
     * @return price deleted
     */
    Price delete(Long priceId);

    List<Price> findPriceListByPriceDistrict(Double min, Double max);

    List<Price> findPriceListByPriceAndItem(Double min,Double max,List<Long> item);

    List<Price>  findPriceListByPriceMin(Double min,List<Long> item);
}
