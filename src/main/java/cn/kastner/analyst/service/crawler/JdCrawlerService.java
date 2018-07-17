package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;


/**
 * 京东爬虫服务
 */
public interface JdCrawlerService {

    /**
     * 爬取商品基本信息
     * @param url 商品链接
     * @return 商品对象
     * @throws Exception 网络错误
     */
    Item crawl(String url) throws Exception;

    /**
     * 爬取商品评论
     * @param item 商品对象
     * @throws Exception 网络错误
     */
    void crawItemComment (Item item) throws Exception;

}
