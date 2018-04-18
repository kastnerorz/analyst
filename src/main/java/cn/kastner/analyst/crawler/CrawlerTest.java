package cn.kastner.analyst.crawler;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerTest {
    public static void main(String [] args) {
        String url = "https://item.jd.com/2357091.html";
        Document doc = new Document("");
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
            String itemCname = matcher.group();
            System.out.println(itemCname);

        }
    }
}
