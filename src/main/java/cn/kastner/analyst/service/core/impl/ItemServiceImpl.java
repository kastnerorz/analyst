package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.service.core.ItemService;

import java.util.List;

public class ItemServiceImpl implements ItemService {

    final private ItemRepository itemRepository;

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
}
