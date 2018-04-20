package cn.kastner.analyst.crawler;

import cn.kastner.analyst.crawler.JdCrawler;
import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.Price;
import cn.kastner.analyst.repository.CommentContentRepository;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.repository.PriceRepository;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTest {

    @Autowired
    static JdCrawler jdCrawler;

    public static void main(String [] args) {
        String itemId = jdCrawler.crawItemByUrl("http://item.jd.com/16580586466.html");


    }
}
