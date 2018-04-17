package cn.kastner.analyst.crawler;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import org.jsoup.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class JdCrawler {

    @Autowired
    ItemRepository itemRepository;

    public String crawItem (String url) {
        String itemName = crawItem(url);
        List<Item> items = itemRepository.findItemByCnameContaining(itemName);
        if (items != null) {
            return items.get(0).getItemId();
        }
        Item item = new Item();
        // crawling here
        item = itemRepository.save(item);
        itemRepository.flush();
        return item.getItemId();
    }

    public String crawItemInfo (String url) {
        String itemName = "";
        // crawling here
        return itemName;
    }

    public void crawItemComments (String url) {

    }
}
