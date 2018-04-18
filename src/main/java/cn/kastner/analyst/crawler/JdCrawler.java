package cn.kastner.analyst.crawler;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.ItemRepository;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdCrawler {

    @Autowired
    ItemRepository itemRepository;

    /**
     * @param url
     * @return itemId
     */
    public String crawItem (String url) {
        Document doc = new Document("");
        String itemCname = "";
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String title = doc.title();
        Pattern pattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        Matcher matcher = pattern.matcher(title);
        Boolean hasItemCname = matcher.find();
        if (hasItemCname) {
            itemCname = matcher.group();
        }

        // check if has item already
        List<Item> items = itemRepository.findItemByCnameContaining(itemCname);
        if (items != null) {
            return items.get(0).getItemId();
        }

        // new item
        Item item = new Item();
        // crawling here

        item = itemRepository.save(item);
        itemRepository.flush();
        return item.getItemId();
    }
}
