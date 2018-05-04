package cn.kastner.analyst.crawler;

import cn.kastner.analyst.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;

public class CrawlerTest {

    @Autowired
    static JdCrawler jdCrawler;

    public static void main(String [] args) {
        Item item = jdCrawler.crawItemByUrl("http://item.jd.com/16580586466.html");


    }
}
