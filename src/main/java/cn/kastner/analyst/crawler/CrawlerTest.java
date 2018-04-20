package cn.kastner.analyst.crawler;

import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTest {
    public static void main(String [] args) {
//        String url = "https://item.jd.com/2357091.html";
//        Document doc = new Document("");
//        try {
//            doc = Jsoup.connect(url)
//                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
//                    .get();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String title = doc.title();
//        Pattern pattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
//        Matcher matcher = pattern.matcher(title);
//        Boolean hasItemCname = matcher.find();
//        if (hasItemCname) {
//            String itemCname = matcher.group();
//            System.out.println(itemCname);
//Sample Text
//        }
        // get commentVersion from html title
        JdCrawler jdCrawler = new JdCrawler();
        String itemId = jdCrawler.crawItemByUrl("https://item.jd.com/6577495.html?jd_pop=9a11627d-0225-46f3-9693-dba2d671a466&abt=0");


    }
}
