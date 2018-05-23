package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;

public class PhoneDbCrawlerServiceImpl implements PhoneDbCrawlerService {


    @Override
    public Item crawByModelAndRom(String model, String rom) {
        String url = searchByModelAndRom(model, rom);
        return crawDetails(url);
    }

    @Override
    public String searchByModelAndRom(String model, String rom) {
        return null;
    }

    @Override
    public Item crawDetails(String url) {
        return null;
    }
}
