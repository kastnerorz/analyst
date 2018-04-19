package cn.kastner.analyst.crawler;

import cn.kastner.analyst.crawler.JdCrawler;

public class MainCrawler {

    JdCrawler jdCrawler;

    TrustReviewCrawler trustReviewCrawler;

    public String crawItemByUrl (String url) {
        String itemId = jdCrawler.crawItemByUrl(url);
        trustReviewCrawler.crawItemById(itemId);
        return itemId;
    }
}
