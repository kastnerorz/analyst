package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.service.crawler.TrustReviewCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Trust Review 爬虫服务实现
 */
@Service
public class TrustReviewCrawlerServiceImpl implements TrustReviewCrawlerService {

    private final
    ItemRepository itemRepository;

    @Autowired
    public TrustReviewCrawlerServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void crawl(Item item) {
        String itemModel = item.getModel();
    }
}
