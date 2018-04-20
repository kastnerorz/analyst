package cn.kastner.analyst.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainCrawler {

    @Autowired
    JdCrawler jdCrawler;

    @Autowired
    TrustReviewCrawler trustReviewCrawler;

    public String crawItemByUrl (String url) {
        String itemId = jdCrawler.crawItemByUrl(url);
        trustReviewCrawler.crawItemById(itemId);
        return itemId;
    }
}
