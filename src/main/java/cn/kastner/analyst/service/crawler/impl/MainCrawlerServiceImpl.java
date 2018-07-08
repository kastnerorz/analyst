package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Item;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public Item crawItemByUrl (String url){
        try {
            item = jdCrawlerService.crawItemByUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (item != null) {
            if (item.getCategory().getLevelThreeName().equals("手机")) {
                try {
                    phoneDbCrawlerService.crawByItem(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            trustReviewCrawlerService.crawByItem(item);
        }
        return item;
    }
}
