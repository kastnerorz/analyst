package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.service.crawler.TrustReviewCrawlerService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainCrawlerServiceImpl implements MainCrawlerService {

    private final
    JdCrawlerService jdCrawlerService;

    private final
    TrustReviewCrawlerService trustReviewCrawlerService;

    @Autowired
    public MainCrawlerServiceImpl(JdCrawlerService jdCrawlerService, TrustReviewCrawlerService trustReviewCrawlerService) {
        this.jdCrawlerService = jdCrawlerService;
        this.trustReviewCrawlerService = trustReviewCrawlerService;
    }

    @Override
    public Item crawItemByUrl (String url){
        Item item = null;
        try {
            item = jdCrawlerService.crawItemByUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        trustReviewCrawlerService.crawItemById(item);
        return item;
    }
}
