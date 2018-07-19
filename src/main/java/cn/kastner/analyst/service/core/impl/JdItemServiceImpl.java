package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.JdItem;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.repository.core.JdItemRepository;
import cn.kastner.analyst.service.core.JdItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdItemServiceImpl implements JdItemService {

    private final JdItemRepository jdItemRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public JdItemServiceImpl(JdItemRepository jdItemRepository, ItemRepository itemRepository) {
        this.jdItemRepository = jdItemRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public JdItem insertByJdItem(JdItem jdItem) {
        return jdItemRepository.save(jdItem);
    }

    @Override
    public JdItem findById(Long jdItemId) {
        return jdItemRepository.findJdItemById(jdItemId);
    }

    @Override
    public JdItem findBySkuId(String skuId) {
        return jdItemRepository.findBySkuId(skuId);
    }

    @Override
    public List<JdItem> findAll() {
        return jdItemRepository.findAll();
    }

    @Override
    public JdItem update(JdItem jdItem) {
        return jdItemRepository.save(jdItem);
    }

    @Override
    public JdItem delete(Long jdItemId) {
        JdItem jdItem = jdItemRepository.findJdItemById(jdItemId);
        jdItemRepository.delete(jdItem);
        return jdItem;
    }
}
