package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.core.Price;

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
     * retrieve all categories
     *
     * @return
     */
    List<Price> findAll();

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
}
