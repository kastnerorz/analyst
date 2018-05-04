package cn.kastner.analyst.crawler;

import cn.kastner.analyst.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainCrawler {

    @Autowired
    JdCrawler jdCrawler;

    @Autowired
    TrustReviewCrawler trustReviewCrawler;

    public Item crawItemByUrl (String url) {
        Item item = jdCrawler.crawItemByUrl(url);
        trustReviewCrawler.crawItemById(item);
        return item;
    }
}
