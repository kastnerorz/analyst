package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;
import org.springframework.stereotype.Service;

/**
 * Trust Review 爬虫服务
 */
@Service
public interface TrustReviewCrawlerService {

    /**
     * @param item 商品对象
     */
    void crawl(Item item);
}
