package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PhoneDbCrawlerServiceImpl implements PhoneDbCrawlerService {

    private static String strClassName = PhoneDbCrawlerServiceImpl.class.getName();
    private static Logger logger = Logger.getLogger(strClassName);

    /**
     * PhoneDB 爬虫主函数
     * @param model 型号
     * @param rom   存储空间
     * @return Item 对象
     */
    @Override
    public Item crawByModelAndRom(String model, String rom) {
        String url = searchByModelAndRom(model, rom);
        return crawDetails(url);
    }

    /**
     * @param model 型号
     * @param rom   存储空间
     * @return PhoneDB详情链接
     */
    @Override
    public String searchByModelAndRom(String model, String rom) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Upgrade-Insecure-Requests", "1");
        Document doc = new Document("http://phonedb.net");
        try {
            doc = Jsoup.connect("/index.php?m=device&s=query")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/66.0.3359.181 Safari/537.36")
                    .headers(headers)
                    .post();
        } catch (IOException e) {
            logger.warning(e.toString());
        }

        Element resultEl = doc.getElementsByAttributeValue("title", "See detailed datasheet")
                            .get(0);
        return resultEl.attr("title");
    }

    /**
     * @param url PhoneDB详情链接
     * @return Item 对象
     */
    @Override
    public Item crawDetails(String url) {
        return null;
    }
}
