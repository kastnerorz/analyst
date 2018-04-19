package cn.kastner.analyst.crawler;
import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.CommentContent;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.repository.CommentRepository;
import cn.kastner.analyst.repository.ItemRepository;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.json.JSONArray;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JdCrawler {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;

    TrustReviewCrawler trustReviewCrawler;
    /**
     * @param url
     * @return itemId
     */
    public String crawItemByUrl (String url) {
        Document doc = new Document("");
        String itemCname = "";
        String itemModel = "";
        Item item;
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // get itemCname from html title
        Pattern cnamePattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        Matcher cnameMatcher = cnamePattern.matcher(doc.title());
        if (cnameMatcher.find()) {
            itemCname = cnameMatcher.group();
        }
        // get itemModel from html title
        Pattern modelPattern = Pattern.compile("\\（(.+)\\）");
        Matcher modelMatcher = modelPattern.matcher(doc.title());
        if (modelMatcher.find()) {
            itemModel = modelMatcher.group();
        }

        // check if has item already
        List<Item> items = itemRepository.findItemByCnameContaining(itemCname);
        if (items != null) {
            item = items.get(0);
        }
        // no item => new item
        item = new Item();

        String itemId = item.getItemId();

        item.setMarketId("1");
        item.setCname(itemCname);
        item.setModel(itemModel);


        // analyse doc
        // get skuid
        String skuid = "";
        Pattern skuidPattern = Pattern.compile("skuid: (\\d*),");
        Matcher skuidMatcher = skuidPattern.matcher(doc.head().toString());
        if (skuidMatcher.find()) {
            skuid = skuidMatcher.group(1);
            item.setSkuId(skuid);
        }

        // get venderId
        String venderId = "";
        Pattern venderIdPattern = Pattern.compile("venderId: (\\d*),");
        Matcher venderIdMatcher = venderIdPattern.matcher(doc.head().toString());
        if (venderIdMatcher.find()) {
            venderId = venderIdMatcher.group(1);
            item.setVenderId(venderId);
        }

        // get category
        String cat = "";
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher(doc.head().toString());
        if (catMatcher.find()) {
            cat = catMatcher.group(1);
        }

        Integer jQueryId = 3456098;
        Long timeStamp = System.currentTimeMillis()/1000;


        // get commentsCount
        Document commentsCountDoc = new Document("");

        try {
            commentsCountDoc = Jsoup.connect("https://club.jd.com/comment/productCommentSummaries.action")
                    .header(":authority", "club.jd.com")
                    .header(":method", "GET")
                    .header(":path", "/comment/productCommentSummaries.action?referenceIds=" + skuid +
                            "&callback=jQuery" + (jQueryId + 1) +
                            "&_=" + System.currentTimeMillis()/1000)
                    .header(":scheme", "https")
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("referer", "https://item.jd.com/6577495.html")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .data("referenceIds", skuid)
                    .data("callback", "jQuery" + jQueryId)
                    .data("_", "" + System.currentTimeMillis() / 1000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject commentsCount;
        Pattern commentsCountPattern = Pattern.compile("^jQuery" + jQueryId + "\\(\\{\\\"CommentsCount\\\":\\[(.+)]\\}\\);$");
        Matcher commentsCountMatcher = commentsCountPattern.matcher(commentsCountDoc.toString());
        if (commentsCountMatcher.find()) {

            commentsCount = new JSONObject(commentsCountMatcher.group(1));

            int generalCount = commentsCount.getInt("GeneralCount");
            item.setGeneralCount(generalCount);

            String generalCountStr = commentsCount.getString("GneralCountStr");
            item.setGeneralCountStr(generalCountStr);

            int goodCount = commentsCount.getInt("GoodCount");
            item.setGoodCount(goodCount);

            String goodCountStr = commentsCount.getString("GoodCountStr");
            item.setGoodCountStr(goodCountStr);

            int poorCount = commentsCount.getInt("PoorCount");
            item.setPoorCount(poorCount);

            String poorCountStr = commentsCount.getString("PoorCountStr");
            item.setPoorCountStr(poorCountStr);
        }
        jQueryId = jQueryId + 1;

        // get stock info
        Document stockDoc = new Document("");
        int pduid = 1901035936;
        try {
            stockDoc = Jsoup.connect("https://c0.3.cn/stock")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Connection", "keep-alive")
                    .header("Host", "c0.3.cn")
                    .header("Referer", "https://item.jd.com/6577495.html")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .data("skuId", skuid)
                    .data("area", "1_72_2799_0")
                    .data("venderId", venderId)
                    .data("cat", cat)
                    .data("buyNum", "1")
                    .data("choseSuitSkuIds", "")
                    .data("extraParam", "{\"originid\":\"1\"}")
                    .data("ch", "1")
                    .data("pduid", "" + (System.currentTimeMillis() / 1000) + pduid )
                    .data("pdpin", "")
                    .data("detailedAdd", "null")
                    .data("callback", "jQuery" + jQueryId)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern stockPattern = Pattern.compile("^jQuery" + jQueryId + "\\(\\{\\\"stock\\\":\\[(.+)]\\}\\);$");
        Matcher stockMatcher = stockPattern.matcher(stockDoc.toString());
        if (stockMatcher.find()) {
            JSONObject stock = new JSONObject(stockMatcher.group(1));

            JSONObject selfDeliver = stock.getJSONObject("self_D");
            String vender = selfDeliver.getString("vender");
            item.setVender(vender);

            JSONObject jdPrice = stock.getJSONObject("jdPrice");
            String p = jdPrice.getString("p");
            // has plus price


        }

        itemRepository.save(item);

        return itemId;
    }
}
