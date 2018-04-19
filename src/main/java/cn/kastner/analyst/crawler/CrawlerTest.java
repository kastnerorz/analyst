package cn.kastner.analyst.crawler;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.IOException;
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
        String cat = "";
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher("t17188/348/549404753/23939/99deacd5/5a9679d8Nc56087b7.jpg\"],\n" +
                "                cat: [9987, 653, 655],\n" +
                "                forceAdUpdate: '8277',");
        if (catMatcher.find()) {
            cat = catMatcher.group(1);
            System.out.println(cat);
        }


    }
}
