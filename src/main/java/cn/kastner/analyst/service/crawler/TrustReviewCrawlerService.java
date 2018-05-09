package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;
import org.springframework.stereotype.Service;

@Service
public interface TrustReviewCrawlerService {

    void crawItemById (Item item);
}
