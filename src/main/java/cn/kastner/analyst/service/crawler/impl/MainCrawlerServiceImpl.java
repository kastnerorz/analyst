package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.service.crawler.TrustReviewCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainCrawlerServiceImpl implements MainCrawlerService {

    @Autowired
    JdCrawlerService jdCrawlerService;

    @Autowired
    TrustReviewCrawlerService trustReviewCrawlerService;

    @Override
    public Item crawItemByUrl (String url) {
        Item item = jdCrawlerService.crawItemByUrl(url);
        trustReviewCrawlerService.crawItemById(item);
        return item;
    }
}
