package cn.kastner.analyst.crawler;

import org.springframework.beans.factory.annotation.Autowired;

public class CrawlerTest {

    @Autowired
    static JdCrawler jdCrawler;

    public static void main(String [] args) {
        String itemId = jdCrawler.crawItemByUrl("http://item.jd.com/16580586466.html");


    }
}
