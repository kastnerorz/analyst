package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.core.Item;


public interface JdCrawlerService {

    Item crawItemByUrl(String url) throws Exception;

    void crawItemComment (Item item) throws Exception;

}
