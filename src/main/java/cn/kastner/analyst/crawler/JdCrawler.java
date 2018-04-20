package cn.kastner.analyst.crawler;
import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.CommentContent;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;
import cn.kastner.analyst.repository.CommentContentRepository;
import cn.kastner.analyst.repository.CommentRepository;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.repository.PriceRepository;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import sun.nio.cs.ext.GBK;


import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JdCrawler {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentContentRepository commentContentRepository;

    @Autowired
    PriceRepository priceRepository;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * @param url
     * @return itemId
     */
    public String crawItemByUrl (String url) {
        Document doc = new Document("");
        String marketId = "1";
        String itemCname = "";
        String itemModel = "";
        String commentVersion = "";
        Item item;
        try {
            System.out.println("Connecting to main url ...");
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
            System.out.println("Get Item Cname from title: " + itemCname);
        }
        // get itemModel from html title
        Pattern modelPattern = Pattern.compile("\\（(.+)\\）");
        Matcher modelMatcher = modelPattern.matcher(doc.title());
        if (modelMatcher.find()) {
            itemModel = modelMatcher.group();
            System.out.println("Get Item Model from title: " + itemModel);
        }

        // get commentVersion from html title
        Pattern commentVersionPattern = Pattern.compile("commentVersion: \\'(\\d+)\\',");
        Matcher commentVersionMatcher = commentVersionPattern.matcher(doc.title());
        if (commentVersionMatcher.find()) {
            commentVersion = commentVersionMatcher.group(1);
            System.out.println("Get comment version from title: " + commentVersion);
        }

        // check if has item already
//        List<Item> items = itemRepository.findItemByCnameContaining(itemCname);
//        if (items != null) {
//            item = items.get(0);
//        }
        // no item => new item
        item = new Item();

        String itemId = item.getItemId();

        item.setMarketId(marketId);
        item.setCname(itemCname);
        item.setModel(itemModel);


        // analyse doc
        // get skuid
        String skuid = "";
        Pattern skuidPattern = Pattern.compile("skuid: (\\d*),");
        Matcher skuidMatcher = skuidPattern.matcher(doc.head().toString());
        if (skuidMatcher.find()) {
            skuid = skuidMatcher.group(1);
            System.out.println("Get skuid from head: " + skuid);
            item.setSkuId(skuid);
        }

        // get venderId
        String venderId = "";
        Pattern venderIdPattern = Pattern.compile("venderId: (\\d+),");
//        Matcher venderIdMatcher = venderIdPattern.matcher(doc.head().toString());
        Matcher venderIdMatcher = venderIdPattern.matcher("pTag: 424177, isPop: false,\n" +
                "                venderId: 1000003443,\n" +
                "                shopId: '1000003443',");

        if (venderIdMatcher.find()) {
            venderId = venderIdMatcher.group(1);
            System.out.println("Get  venderId from head: " + venderId);
            item.setVenderId(venderId);
        } else {
            System.out.println("no venderId");
        }

        // get category
        String cat = "";
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher(doc.head().toString());
        if (catMatcher.find()) {
            cat = catMatcher.group(1);
            System.out.println("Get cat from head: " + cat);
        }

        Integer jQueryId = 3456098;
        Long timeStamp = System.currentTimeMillis()/1000;


        // get commentsCount
        Document commentsCountDoc = new Document("");

        try {
            System.out.println("Connecting to https://club.jd.com/comment/productCommentSummaries.action");
            commentsCountDoc = Jsoup.connect("https://club.jd.com/comment/productCommentSummaries.action")
//                    .header(":authority", "club.jd.com")
//                    .header(":method", "GET")
//                    .header(":path", "/comment/productCommentSummaries.action?referenceIds=" + skuid +
//                            "&callback=jQuery" + (jQueryId + 1) +
//                            "&_=" + System.currentTimeMillis()/1000)
//                    .header(":scheme", "https")
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("referer", "https://item.jd.com/" + itemId + ".html")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .data("referenceIds", skuid)
                    .data("callback", "jQuery" + jQueryId)
                    .data("_", "" + System.currentTimeMillis() / 1000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject commentsCount;
        int generalCount = 0;
        Pattern commentsCountPattern = Pattern.compile("jQuery" + jQueryId + "\\(\\{\\\"CommentsCount\\\":\\[(.+)]\\}\\);");
        Matcher commentsCountMatcher = commentsCountPattern.matcher(commentsCountDoc.toString());
        if (commentsCountMatcher.find()) {
            System.out.println("analysing");

            commentsCount = new JSONObject(commentsCountMatcher.group(1));

            generalCount = commentsCount.getInt("GeneralCount");
            System.out.println("gneralCount: " + generalCount);

            String generalCountStr = commentsCount.getString("GeneralCountStr");

            int goodCount = commentsCount.getInt("GoodCount");

            String goodCountStr = commentsCount.getString("GoodCountStr");

            int poorCount = commentsCount.getInt("PoorCount");

            String poorCountStr = commentsCount.getString("PoorCountStr");
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
                    .ignoreContentType(true)
                    .get();

            String stockStr = new String(stockDoc.toString().getBytes(), "gbk");
            System.out.println(stockStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern stockPattern = Pattern.compile("jQuery" + jQueryId + "\\(\\{\\\"stock\\\":(\\{.+\\}),\\\"");
        Matcher stockMatcher = stockPattern.matcher(stockDoc.text());
        if (stockMatcher.find()) {
            JSONObject stock = new JSONObject(stockMatcher.group(1));

            JSONObject selfDeliver = stock.getJSONObject("self_D");
            String vender = selfDeliver.getString("vender");
            item.setVender(vender);

            JSONObject jdPrice = stock.getJSONObject("jdPrice");
            String p = jdPrice.getString("p");
            // has plus price
            Price price = new Price();
            price.setPrice(Double.parseDouble(p));
            price.setItemId(itemId);
            price.setMarketId(marketId);
            Long volume = (long) generalCount;
            price.setVolume(volume);
            Date now = new Date();
            price.setDate(now);
            priceRepository.save(price);
        }

        // get comments
        Document commentDoc = new Document("");
        for (int currentPage = 0; currentPage < generalCount / 10; currentPage++) {
            try {
                System.out.println("Connecting to https://club.jd.com/comment/skuProductPageComments.action");
                commentDoc = Jsoup.connect("http://club.jd.com/comment/skuProductPageComments.action")
//                        .header(":authority", "club.jd.com")
//                        .header(":method","GET")
//                        .header(":path", "/comment/skuProductPageComments.action?callback=fetchJSON_comment98vv" + commentVersion +
//                                "&productId=" + itemId +
//                                "&score=0&sortType=5" +
//                                "&page=" + currentPage +
//                                "&pageSize=10&isShadowSku=0&rid=0&fold=1")
//                        .header(":scheme", "https")
                        .header("accept","*/*")
                        .header("accept-encoding","gzip, deflate, br")
                        .header("accept-language","zh-CN,zh;q=0.9")
                        .header("referer","https://item.jd.com/" + itemId + ".html")
                        .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .data("callback", "fetchJSON_comment98vv" + commentVersion)
                        .data("productId", itemId)
                        .data("score","0")
                        .data("sortType","5")
                        .data("page", "" + currentPage)
                        .data("pageSize", "10")
                        .data("isShadowSku","0")
                        .data("rid","0")
                        .data("fold","1")
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pattern commentPattern = Pattern.compile("fetchJSON_comment98vv3781\\((\\{.+\\})\\);");
            Matcher commentMatcher = commentPattern.matcher(commentDoc.toString());
            if (commentMatcher.find()) {
                JSONObject comment = new JSONObject(commentMatcher.group(1));
                if (0 == currentPage) {
                    JSONObject productCommentsCount = comment.getJSONObject("productCommentSummary");

                    int commentCount = productCommentsCount.getInt("commentCount");
                    item.setCommentCount(commentCount);

                    String commentCountStr = productCommentsCount.getString("commentCountStr");
                    item.setCommentCountStr(commentCountStr);

                    generalCount = productCommentsCount.getInt("generalCount");
                    item.setGeneralCount(generalCount);

                    String generalCountStr = productCommentsCount.getString("gneralCountStr");
                    item.setGeneralCountStr(generalCountStr);

                    int goodCount = productCommentsCount.getInt("goodCount");
                    item.setGoodCount(goodCount);

                    String goodCountStr = productCommentsCount.getString("goodCountStr");
                    item.setGoodCountStr(goodCountStr);

                    int poorCount = productCommentsCount.getInt("poorCount");
                    item.setPoorCount(poorCount);

                    String poorCountStr = productCommentsCount.getString("poorCountStr");
                    item.setPoorCountStr(poorCountStr);
                }
                JSONArray comments = comment.getJSONArray("comments");
                for (int i = 0; i < comments.length(); i++) {
                    JSONObject cmt = comments.getJSONObject(i);
                    CommentContent commentContent = new CommentContent();
                    commentContent.setItemId(itemId);

                    String content = cmt.getString("content");
                    commentContent.setContent(content);

                    commentContentRepository.save(commentContent);
                }
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        item.setAdvan("1");
        item.setDisAdvan("1");
        item.setKeyFeature("1");
        item.setEname("1");
        item.setBrandId("1");
        itemRepository.save(item);

        return itemId;
    }
}
