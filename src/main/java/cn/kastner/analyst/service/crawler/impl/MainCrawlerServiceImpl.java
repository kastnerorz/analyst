package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import cn.kastner.analyst.service.crawler.TrustReviewCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 爬虫服务主入口实现
 */
@Service
public class MainCrawlerServiceImpl implements MainCrawlerService {

    private final
    JdCrawlerService jdCrawlerService;

    private final
    TrustReviewCrawlerService trustReviewCrawlerService;

    private final PhoneDbCrawlerService phoneDbCrawlerService;

    @Autowired
    public MainCrawlerServiceImpl(JdCrawlerService jdCrawlerService, TrustReviewCrawlerService trustReviewCrawlerService, PhoneDbCrawlerService phoneDbCrawlerService) {
        this.jdCrawlerService = jdCrawlerService;
        this.trustReviewCrawlerService = trustReviewCrawlerService;
        this.phoneDbCrawlerService = phoneDbCrawlerService;
    }

    private Item item;

    @Override
    public Item crawl(String url){
        try {
            item = jdCrawlerService.crawl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (item != null) {
            if (item.getCategory().getLevelThreeName().equals("手机")) {
                try {
                    phoneDbCrawlerService.crawl(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            trustReviewCrawlerService.crawl(item);
        }
        return item;
    }
}
