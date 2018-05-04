package cn.kastner.analyst.crawler;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrustReviewCrawler {

    @Autowired
    ItemRepository itemRepository;

    public void crawItemById (Item item) {
        String itemModel = item.getModel();
    }
}
