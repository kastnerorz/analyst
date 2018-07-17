package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;


/**
 * 京东爬虫服务
 */
public interface JdCrawlerService {

    /**
     * 商品主要信息抓取
     * @param url 商品链接
     * @return 商品对象
     * @throws Exception 网络错误
     */
    Item crawl(String url) throws Exception;

    /**
     * 商品评论抓取
     * @param item 商品对象
     * @throws Exception 网络错误
     */
    void crawlItemComment(Item item) throws Exception;

}
