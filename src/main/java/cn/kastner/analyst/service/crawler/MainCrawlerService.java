package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;
import org.json.JSONException;

/**
 * 爬虫服务主入口
 */
public interface MainCrawlerService {

    /**
     * 通过链接爬取商品
     * @param url 商品链接
     * @return 商品对象
     * @throws JSONException
     */
    Item crawl(String url) throws JSONException;
}
