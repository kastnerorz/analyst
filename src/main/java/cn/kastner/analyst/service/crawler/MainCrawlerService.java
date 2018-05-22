package cn.kastner.analyst.service.crawler;

import cn.kastner.analyst.domain.core.Item;
import org.json.JSONException;

public interface MainCrawlerService {

    Item crawItemByUrl (String url) throws JSONException;
}
