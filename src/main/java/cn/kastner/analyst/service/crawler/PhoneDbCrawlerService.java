package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;

import java.io.IOException;

/**
 * PhoneDB 爬虫服务
 */
public interface PhoneDbCrawlerService {

    /**
     * @param item 商品
     * @return 商品对象
     */
    Item crawl(Item item) throws IOException;

    /**
     * @param model 型号
     * @param rom 存储空间
     * @return PhoneDB详情链接
     */
    String searchByModelAndRom (String model, String rom) throws IOException;

    /**
     * @param url PhoneDB详情链接
     */
    void crawDetails (String url) throws IOException;
}
