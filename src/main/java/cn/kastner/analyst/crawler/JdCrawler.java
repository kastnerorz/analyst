package cn.kastner.analyst.crawler;

import cn.kastner.analyst.domain.Comment;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Market;
import cn.kastner.analyst.domain.Price;
import cn.kastner.analyst.repository.BrandRepository;
import cn.kastner.analyst.repository.CommentRepository;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.repository.PriceRepository;
import org.jsoup.*;
import org.jsoup.Connection.*;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;


@Service
public class JdCrawler {

    private static String strClassName = JdCrawler.class.getName();
    private static Logger logger = Logger.getLogger(strClassName);

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    BrandRepository brandRepository;


  @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * @param url
     * @return itemId
     */
    public String crawItemByUrl(String url) {
        Document doc = new Document("");
        String marketId = "1";
        String commentVersion = "";
        Item item = new Item();

        try {
            logger.info("Connecting to base url ...");
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        } catch (IOException e) {
            logger.warning(String.valueOf(e.getStackTrace()));
        }

        // get itemCname from html title
        Pattern cnamePattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        Matcher cnameMatcher = cnamePattern.matcher(doc.title());
        if (cnameMatcher.find()) {
            String zhName = cnameMatcher.group();
            item.setZhName(zhName);
            logger.info("Get Item Cname from title: " + zhName);
        }


        // get itemModel from html title
        Pattern modelPattern = Pattern.compile("\\（(.+)\\）");
        Matcher modelMatcher = modelPattern.matcher(doc.title());
        if (modelMatcher.find()) {
            String model = modelMatcher.group(1);
            item.setModel(model);
            logger.info("Get Item Model from title: " + model);
        }


        // check if has item already
        List<Item> items = itemRepository.findByZhName(item.getZhName());
        if (items.size() != 0) {
            item = items.get(0);
            return item.getItemId();
        } else {
            item = new Item();
        }
        // no item => new item

        String itemId = item.getItemId();

        Market market = new Market("京东");
        item.setMarket(market);
        item.setZhName(itemCname);
        item.setModel(model);


        // analyse doc
        // get skuid
        String skuid = "";
        Pattern skuidPattern = Pattern.compile("skuid: (\\d*),");
        Matcher skuidMatcher = skuidPattern.matcher(doc.head().toString());
        if (skuidMatcher.find()) {
            skuid = skuidMatcher.group(1);
            logger.info("Get skuid from head: " + skuid);
            item.setSkuId(skuid);
        }

        // get&set vendorId
        String vendorId = "";
        Pattern vendorIdPattern = Pattern.compile("vendorId:(\\d+),");
        Matcher vendorIdMatcher = vendorIdPattern.matcher(doc.head().toString());
        if (vendorIdMatcher.find()) {
            vendorId = vendorIdMatcher.group(1);
            logger.info("Get  vendorId from head: " + vendorId);
            item.setVendorId(vendorId);
        } else {
            logger.warning("no vendorId");
        }

        // get imageList\"
        String imageList = "";
        Pattern imageListPattern = Pattern.compile("imageList: \\[\\\"(.+)\\\"\\]");
        Matcher imageListMatcher = imageListPattern.matcher(doc.head().toString());
        if (imageListMatcher.find()) {
            imageList = imageListMatcher.group(1);
            logger.info("Get  imageList from head: " + imageList);
            item.setImageList(imageList);
        } else {
            logger.warning("no imageList");
        }

        // get commentVersion from html head
        Pattern commentVersionPattern = Pattern.compile("commentVersion:\\'(.+)\\',");
        Matcher commentVersionMatcher = commentVersionPattern.matcher(doc.head().toString());
        if (commentVersionMatcher.find()) {
            commentVersion = commentVersionMatcher.group(1);
            logger.info("Get comment version from head: " + commentVersion);
        } else {
            logger.warning("no commentVersion");
        }

        // get category
        String cat = "";
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher(doc.head().toString());
        if (catMatcher.find()) {
            cat = catMatcher.group(1);
            item.setCategory(cat);
            logger.info("Get cat from head: " + cat);
        }

        Integer jQueryId = 6546654;


        // get commentsCount
        Document commentsCountDoc = new Document("");

        try {
            logger.info("Connecting to http://club.jd.com/comment/productCommentSummaries.action");
            commentsCountDoc = Jsoup.connect("http://club.jd.com/comment/productCommentSummaries.action")
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
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/65.0.3325.181 Safari/537.36")

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
            logger.info("analysing");

            commentsCount = new JSONObject(commentsCountMatcher.group(1));

            generalCount = commentsCount.getInt("GeneralCount");
            logger.info("gneralCount: " + generalCount);

            String generalCountStr = commentsCount.getString("GeneralCountStr");

            int goodCount = commentsCount.getInt("GoodCount");

            String goodCountStr = commentsCount.getString("GoodCountStr");

            int poorCount = commentsCount.getInt("PoorCount");

            String poorCountStr = commentsCount.getString("PoorCountStr");
        }
        jQueryId = jQueryId + 1;


        // get stock info
        String stockStr = "";
        int pduid = 1901035936;
        try {
//            URL stockUrl = new URL("https://c0.3.cn/stock?skuid=" + skuid +
//                                    "&area=1_72_2799_0" +
//                                    "&vendorId=" + vendorId +
//                                    "&cat=" + cat +
//                                    "&buyNum=1" +
//                                    "&choseSuitSkuIds=" +
//                                    "&extraParam={\"originid\":\"1\"}" +
//                                    "&ch=1" +
//                                    "&pduid=" + (System.currentTimeMillis() / 1000) + pduid +
//                                    "&pdpin=" +
//                                    "&detailedAdd=null" +
//                                    "&callback=jQuery" + jQueryId);
//            HttpsURLConnection connection = (HttpsURLConnection)stockUrl.openConnection();
//            connection.addRequestProperty("contentType", "text");
//            connection.setRequestMethod("GET");
//            connection.addRequestProperty("accept", "*/*");
//            connection.addRequestProperty("accept-encoding", "gzip, deflate, br");
//            connection.addRequestProperty("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
//            connection.addRequestProperty("Connection", "keep-alive");
//            connection.addRequestProperty("Host", "c0.3.cn");
//            connection.addRequestProperty("referer", "https://item.jd.com/" + itemId + ".html");
//            connection.addRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
//            stockDoc = Jsoup.parse(connection.getInputStream(), "GBK", "https://club.jd.com/comment/productCommentSummaries.action");


            Response stockDoc = Jsoup.connect("https://c0.3.cn/stock?" + "skuId=" + skuid +
                    "&area=1_72_2799_0" +
                    "&vendorId=" + vendorId +
                    "&cat=" + cat +
                    "&buyNum=1" +
                    "&choseSuitSkuIds=" +
                    "&extraParam={\"originid\":\"1\"}" +
                    "&ch=1" +
                    "&pduid=" + (System.currentTimeMillis() / 1000) + pduid +
                    "&pdpin=" +
                    "&detailedAdd=null" +
                    "&callback=jQuery" + jQueryId)
                    .ignoreContentType(true)
                    .execute();

//            stockDoc = Jsoup.connect("http://c0.3.cn/stock")
//                            .header("Accept", "text/*")
//                            .header("Accept-Encoding", "gzip, deflate, br")
//                            .header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
//                            .header("Connection", "keep-alive")
//                            .header("Host", "c0.3.cn")
//                            .header("Referer", "https://item.jd.com/" + itemId + ".html")
//                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
//                            .data("skuId", skuid)
//                            .data("area", "1_72_2799_0")
//                            .data("vendorId", vendorId)
//                            .data("cat", cat)
//                            .data("buyNum", "1")
//                            .data("choseSuitSkuIds", "{\"originid\":\"1\"}")
//                            .data("extraParam", "")
//                            .data("ch", "1")
//                            .data("pduid", "" + System.currentTimeMillis() + pduid )
//                            .data("pdpin", "")
//                            .data("detailedAdd", "null")
//                            .data("callback", "jQuery" + jQueryId)
//                            .ignoreContentType(true)
//                            .get();
            stockStr = stockDoc.body();
            logger.info(stockStr);
//            String stockStr = new String(stockDoc.toString().getBytes(), "gbk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern stockPattern = Pattern.compile("jQuery" + jQueryId + "\\(\\{\\\"stock\\\":(\\{.+\\}),\\\"");
        Matcher stockMatcher = stockPattern.matcher(stockStr);
        if (stockMatcher.find()) {
            JSONObject stock = new JSONObject(stockMatcher.group(1));

            JSONObject selfDeliver;
            try {
                selfDeliver = stock.getJSONObject("self_D");
                String vender = selfDeliver.getString("vender");
                item.setVendor(vender);
            } catch (Exception e) {
                logger.info("get selfDeliver from D");
            }

            try {
                selfDeliver = stock.getJSONObject("D");
                String vender = selfDeliver.getString("vender");
                item.setVendor(vender);
            } catch (Exception e) {
                logger.info("get selfDeliver from self_D");
            }


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
            logger.info("price_id   ->" + price.getPriceId() + "\n" +
                    "date       ->" + price.getDate() + "\n" +
                    "item_id    ->" + price.getItemId() + "\n" +
                    "market_id  ->" + price.getMarketId() + "\n" +
                    "price      ->" + price.getPrice() + "\n" +
                    "volume     ->" + price.getVolume());
            priceRepository.save(price);
            logger.info("price has been saved.");
        }


        // get comments
        String commentStr = "";
        for (int currentPage = 0; currentPage < 5; currentPage++) {
            try {
                String commentUrl = "https://club.jd.com/comment/skuProductPageComments.action?" +
                        "callback=fetchJSON_comment98vv" + commentVersion +
                        "&productId=" + skuid +
                        "&score=0" +
                        "&sortType=5" +
                        "&page=" + currentPage +
                        "&pageSize=10" +
                        "&isShadowSku=0" +
                        "&rid=0" +
                        "&fold=1";
                logger.info("Connecting to " + commentUrl);
                Response commentDoc = Jsoup.connect(commentUrl)
                        .header("accept", "*/*")
                        .header("accept-encoding", "gzip, deflate, br")
                        .header("accept-language", "zh-CN,zh;q=0.9")
                        .header("referer", "https://item.jd.com/" + skuid + ".html")
                        .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .execute();
                commentStr = commentDoc.body();
                logger.info(commentStr);
            } catch (IOException e) {
                logger.info("Can't connect to http://club.jd.com/comment/skuProductPageComments.action");
                e.printStackTrace();
            }
            Pattern commentPattern = Pattern.compile("fetchJSON_comment98vv" + commentVersion + "\\((\\{.+\\})\\);");
            Matcher commentMatcher = commentPattern.matcher(commentStr);
            if (commentMatcher.find()) {
                JSONObject comment = new JSONObject(commentMatcher.group(1));
                if (0 == currentPage) {
                    JSONObject productCommentsCount = comment.getJSONObject("productCommentSummary");

                    int commentCount = productCommentsCount.getInt("commentCount");
                    item.setCommentCount(commentCount);

                    String commentCountStr = productCommentsCount.getString("commentCountStr");
                    item.setCommentCountStr(commentCountStr);
                    logger.info("[set]commentCountStr    ->" + item.getCommentCountStr());

                    generalCount = productCommentsCount.getInt("generalCount");
                    item.setGeneralCount(generalCount);

                    String generalCountStr = productCommentsCount.getString("generalCountStr");
                    item.setGeneralCountStr(generalCountStr);
                    logger.info("[set]generalCountStr    ->" + item.getGeneralCountStr());

                    int goodCount = productCommentsCount.getInt("goodCount");
                    item.setGoodCount(goodCount);

                    String goodCountStr = productCommentsCount.getString("goodCountStr");
                    item.setGoodCountStr(goodCountStr);
                    logger.info("[set]goodCountStr       ->" + item.getGoodCountStr());


                    int poorCount = productCommentsCount.getInt("poorCount");
                    item.setPoorCount(poorCount);

                    String poorCountStr = productCommentsCount.getString("poorCountStr");
                    item.setPoorCountStr(poorCountStr);
                    logger.info("[set]poorCountStr       ->" + item.getPoorCountStr());

                }
                JSONArray comments = comment.getJSONArray("comments");
                for (int i = 0; i < comments.length(); i++) {
                    JSONObject cmt = comments.getJSONObject(i);
                    Comment commentContent = new Comment();
                    commentContent.setItemId(itemId);

                    String content = cmt.getString("content");
                    commentContent.setContent(content);

//                    logger.info("comment_id     ->" + commentContent.getFeatureId() + "\n" +
//                                "content        ->" + commentContent.getContent() + "\n" +
//                                "is_good        ->" + commentContent.getGood() + "\n" +
//                                "content_id     ->" + commentContent.getContentId() + "\n" +
//                                "item_id        ->" + commentContent.getItemId());
                    commentRepository.save(commentContent);
                    logger.info("commentContent has been saved.");
                }
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("[check]commentCountStr    ->" + item.getCommentCountStr());
        logger.info("[check]generalCountStr    ->" + item.getGeneralCountStr());
        logger.info("[check]goodCountStr       ->" + item.getGoodCountStr());
        logger.info("[check]poorCountStr       ->" + item.getPoorCountStr());
        itemRepository.save(item);
        logger.info("item has been saved.");

        return itemId;
    }
}
