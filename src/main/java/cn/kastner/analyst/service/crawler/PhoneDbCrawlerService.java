package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.core.Item;

/**
 * PhoneDB 爬虫服务
 */
public interface PhoneDbCrawlerService {

    /**
     * @param model 型号
     * @param rom 存储空间
     * @return 商品对象
     */
    Item crawByModelAndRom (String model, String rom);

    /**
     * @param model 型号
     * @param rom 存储空间
     * @return PhoneDB详情链接
     */
    String searchByModelAndRom (String model, String rom);

    /**
     * @param url PhoneDB详情链接
     * @return 商品对象
     */
    Item crawDetails (String url);
}
