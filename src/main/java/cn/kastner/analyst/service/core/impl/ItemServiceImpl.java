package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.service.core.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item insertByItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findByItemId(itemId);
    }

    @Override
    public Item findBySkuId(String skuId) {
        return itemRepository.findBySkuId(skuId);
    }

    @Override
    public List<Item> findByZhName(String zhName) {
        return itemRepository.findByZhName(zhName);
    }

    @Override
    public List<Item> findByZhNameLike(String zhName) {
        return itemRepository.findByZhNameContaining(zhName);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item update(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item delete(Long itemId) {
        Item item = itemRepository.findByItemId(itemId);
        itemRepository.delete(item);
        return item;
    }

    @Override
    public List<Item> findByCategoryId(Long categoryId){
        return itemRepository.findAllByCategoryCategoryId(categoryId);
    }

    @Override
    public List<Item> findByBrandIdAndCategoryId(Long brandId,Long categoryId){
        return itemRepository.findAllByBrandBrandIdAndCategoryCategoryId(brandId,categoryId);
    }
}

