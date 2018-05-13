package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.*;
import org.json.JSONException;


public interface JdCrawlerService {

    Item crawItemByUrl(String url) throws Exception;

    void crawItemComment (Item item) throws Exception;

}
