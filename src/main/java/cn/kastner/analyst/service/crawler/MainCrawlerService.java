package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.Item;

public interface MainCrawlerService {

    Item crawItemByUrl (String url);
}
