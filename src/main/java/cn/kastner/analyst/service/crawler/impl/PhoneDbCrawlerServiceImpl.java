package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class PhoneDbCrawlerServiceImpl implements PhoneDbCrawlerService {


    @Override
    public Item crawByModelAndRom(String model, String rom) {
        String url = searchByModelAndRom(model, rom);
        return crawDetails(url);
    }

    @Override
    public String searchByModelAndRom(String model, String rom) {
        Map headers = new HashMap();
//        Document dov = Jsoup.connect("http://phonedb.net/index.php?m=device&s=query")
//                .post();

        return null;
    }

    @Override
    public Item crawDetails(String url) {
        return null;
    }
}
