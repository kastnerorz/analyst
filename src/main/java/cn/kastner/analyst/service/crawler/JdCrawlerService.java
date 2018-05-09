package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.*;


public interface JdCrawlerService {

    Item crawItemByUrl(String url);

    void crawItemComment (Item item);

}
