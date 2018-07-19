package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;
import cn.kastner.analyst.repository.core.PriceRepository;
import cn.kastner.analyst.service.core.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price insertByPrice(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public Price findById(Long priceId) {
        return priceRepository.findByPriceId(priceId);
    }

    @Override
    public List<Price> findByItemAndCrawDateTimeAfter(Item item, LocalDateTime crawDateTime) {
        return priceRepository.findByItemAndCrawDateTimeAfter(item, crawDateTime);
    }

    @Override
    public Price findLatestByItem(Item item) {
        return priceRepository.findByItemOrderByCrawDateTimeDesc(item).get(0);
    }

    @Override
    public List<Price> findByItem(Item item) {
        return priceRepository.findByItem(item);
    }

    @Override
    public Price update(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public Price delete(Long priceId) {
        Price price = priceRepository.findByPriceId(priceId);
        priceRepository.delete(price);
        return price;
    }

    @Override
    public List<Price> findPriceListByPriceDistrict(Double min, Double max){
        return priceRepository.findAllByPriceBetween(min,max);
    }

    @Override
    public List<Price> findPriceListByPriceAndItem(Double min,Double max,List<Long> item){
        return priceRepository.findAllByPriceBetweenAndItemIdIn(min,max,item);
    }

    @Override
    public List<Price> findPriceListByPriceMin(Double min ,List<Long> item){
        return priceRepository.findAllByPriceGreaterThanAndItemIdIn(min,item);

    }

}
