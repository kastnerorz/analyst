package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import cn.kastner.analyst.repository.core.PriceRepository;
import cn.kastner.analyst.service.core.PriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

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
    public List<Price> findByItemAndCrawDateTime(Item item, LocalDateTime crawDateTime) {
        return priceRepository.findPricesByItemAndCrawDateTimeAfter(item, crawDateTime);
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
    public List<Price> findByItemId(Long itemId){
        return priceRepository.findByItemItemId(itemId);
   }
}
