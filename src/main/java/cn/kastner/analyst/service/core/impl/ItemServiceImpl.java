package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.JdItem;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.repository.core.JdItemRepository;
import cn.kastner.analyst.service.core.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final JdItemRepository jdItemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, JdItemRepository jdItemRepository) {
        this.itemRepository = itemRepository;
        this.jdItemRepository = jdItemRepository;
    }

    @Override
    public Item insertByItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepository.findItemById(itemId);
    }

    @Override
    public Item findBySkuId(String skuId) {
        JdItem jdItem =  jdItemRepository.findBySkuId(skuId);
        return itemRepository.findItemById(jdItem.getId());
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
        Item item = itemRepository.findItemById(itemId);
        itemRepository.delete(item);
        return item;
    }

    @Override
    public List<Item> findByCategory(Category category) {
        return itemRepository.findByCategory(category);
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

