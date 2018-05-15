package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.*;
import cn.kastner.analyst.repository.*;
import cn.kastner.analyst.service.core.*;
import cn.kastner.analyst.service.core.impl.*;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JdCrawlerServiceImpl implements JdCrawlerService {

    private static String strClassName = JdCrawlerService.class.getName();
    private static Logger logger = Logger.getLogger(strClassName);

    private final
    ItemService itemService;

    private final
    CommentService commentService;

    private final
    PriceService priceService;

    private final
    BrandService brandService;

    private final
    CategoryService categoryService;

    @Autowired
    public JdCrawlerServiceImpl(ItemService itemService, CommentService commentService, PriceService priceService, BrandService brandService, CategoryService categoryService) {
        this.itemService = itemService;
        this.commentService = commentService;
        this.priceService = priceService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * @param url
     * @return itemId
     */
    @Override
    public Item crawItemByUrl(String url) throws Exception {


        Item item = new Item();
        Market market = new Market(Market.Code.JD);
        item.setMarket(market);

        Document doc = new Document("");
        try {
            logger.info("Connecting to base url ...");
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        } catch (IOException e) {
            logger.warning(String.valueOf(e.getStackTrace()));
        }

        // get skuid
        Pattern skuidPattern = Pattern.compile("skuid: (\\d*),");
        Matcher skuidMatcher = skuidPattern.matcher(doc.head().toString());
        if (skuidMatcher.find()) {
            String skuid = skuidMatcher.group(1);
            item.setSkuId(skuid);
            logger.info("Get skuid from head: " + skuid);
        } else {
            logger.warning("can't get skuid");
        }


        // check if has item already
        Item itemDb = itemService.findBySkuId(item.getSkuId());
        if (null != itemDb) {

            // TODO if timestamp is expired then craw again else return

            return itemDb;
        }


        // get itemCname from html title
        Pattern cnamePattern = Pattern.compile("(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        Matcher cnameMatcher = cnamePattern.matcher(doc.title());
        if (cnameMatcher.find()) {
            String zhName = cnameMatcher.group();
            item.setZhName(zhName);
            logger.info("Get Item zhName from title: " + zhName);
        } else {
            logger.warning("Can't get item zhName");
        }


        // get itemModel from html title
        Pattern modelPattern = Pattern.compile("\\（(.+)\\）");
        Matcher modelMatcher = modelPattern.matcher(doc.title());
        if (modelMatcher.find()) {
            String model = modelMatcher.group(1);
            item.setModel(model);
            logger.info("Get Item Model from title: " + model);
        } else {
            logger.warning("Can't get item model");
        }




        Long itemId = item.getItemId();





        // analyse doc


        // get&set vendorId
        Pattern vendorIdPattern = Pattern.compile("venderId:(\\d+),");
        Matcher vendorIdMatcher = vendorIdPattern.matcher(doc.head().toString());
        if (vendorIdMatcher.find()) {
            String vendorId = vendorIdMatcher.group(1);
            item.setVendorId(vendorId);
            logger.info("Get  vendorId from head: " + vendorId);
        } else {
            logger.warning("no vendorId");
        }

        // get imageList

        Pattern imageListPattern = Pattern.compile("imageList: \\[\\\"(.+)\\\"\\]");
        Matcher imageListMatcher = imageListPattern.matcher(doc.head().toString());
        if (imageListMatcher.find()) {
            String imageList = imageListMatcher.group(1);

            // TODO deal with imageList

            item.setImageList(imageList);
            logger.info("Get  imageList from head: " + imageList);
        } else {
            logger.warning("no imageList");
        }


        // get commentVersion from html head
        Pattern commentVersionPattern = Pattern.compile("commentVersion:\\'(.+)\\',");
        Matcher commentVersionMatcher = commentVersionPattern.matcher(doc.head().toString());
        if (commentVersionMatcher.find()) {
            String commentVersion = commentVersionMatcher.group(1);
            item.setCommentVersion(commentVersion);
            logger.info("Get comment version from head: " + commentVersion);
        } else {
            logger.warning("no commentVersion");
        }

        // get category
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher(doc.head().toString());
        if (catMatcher.find()) {
            String categoryStr = catMatcher.group(1);
            String[] cats = categoryStr.split(",");
            Category categoryDb = categoryService.findByLevels(
                    Integer.parseInt(cats[0]),
                    Integer.parseInt(cats[1]),
                    Integer.parseInt(cats[2])
            );
            Category category;
            if (null != categoryDb) {
                category = categoryDb;
            } else {
                category = new Category();
            }
            category.setCategoryStr(categoryStr);
            category.setLevelOne(Integer.parseInt(cats[0]));
            category.setLevelTwo(Integer.parseInt(cats[1]));
            category.setLevelThree(Integer.parseInt(cats[2]));

//            if (null == categoryDb) {
//                categoryService.save(category);
//            }
            item.setCategory(category);
            logger.info("Get cat from head: " + categoryStr);
        } else {
            logger.warning("no category");
        }

        Integer jQueryId = 6546654;


        // get commentsCount
        Document commentsCountDoc = new Document("");

        try {
            logger.info("Connecting to http://club.jd.com/comment/productCommentSummaries.action");
            commentsCountDoc = Jsoup.connect("http://club.jd.com/comment/productCommentSummaries.action")
                    .header("accept", "*/*")
                    .header("accept-encoding", "gzip, deflate, br")
                    .header("accept-language", "zh-CN,zh;q=0.9")
                    .header("referer", "https://item.jd.com/" + itemId + ".html")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/65.0.3325.181 Safari/537.36")

                    .data("referenceIds", item.getSkuId())
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
            Connection.Response stockDoc = Jsoup.connect("https://c0.3.cn/stock?" + "skuId=" + item.getSkuId() +
                    "&area=1_72_2799_0" +
                    "&vendorId=" + item.getVendorId() +
                    "&cat=" + item.getCategory().getCategoryStr() +
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
            price.setItem(item);
            price.setMarket(market);

            Long volume = (long) generalCount;
            price.setVolume(volume);


            price.setDateTime(LocalDateTime.now());
            logger.info("price_id   ->" + price.getPriceId() + "\n" +
                    "date       ->" + price.getDateTime() + "\n" +
                    "item       ->" + price.getItem() + "\n" +
                    "market     ->" + price.getMarket() + "\n" +
                    "price      ->" + price.getPrice() + "\n" +
                    "volume     ->" + price.getVolume());
            priceService.insertByPrice(price);
            logger.info("price has been saved.");
        }


        itemService.insertByItem(item);
        logger.info("item has been saved.");

        return item;
    }

    /**
     * @param item
     */
    @Override
    public void crawItemComment (Item item) throws Exception {
        // get comments
        String commentStr = "";
        for (int currentPage = 0; currentPage < 5; currentPage++) {
            try {
                String commentUrl = "https://club.jd.com/comment/skuProductPageComments.action?" +
                        "callback=fetchJSON_comment98vv" + item.getCommentVersion() +
                        "&productId=" + item.getSkuId() +
                        "&score=0" +
                        "&sortType=5" +
                        "&page=" + currentPage +
                        "&pageSize=10" +
                        "&isShadowSku=0" +
                        "&rid=0" +
                        "&fold=1";
                logger.info("Connecting to " + commentUrl);
                Connection.Response commentDoc = Jsoup.connect(commentUrl)
                        .header("accept", "*/*")
                        .header("accept-encoding", "gzip, deflate, br")
                        .header("accept-language", "zh-CN,zh;q=0.9")
                        .header("referer", "https://item.jd.com/" + item.getSkuId() + ".html")
                        .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                        .execute();
                commentStr = commentDoc.body();
                logger.info(commentStr);
            } catch (IOException e) {
                logger.info("Can't connect to http://club.jd.com/comment/skuProductPageComments.action");
                e.printStackTrace();
            }
            Pattern commentPattern = Pattern.compile("fetchJSON_comment98vv" + item.getCommentVersion() + "\\((\\{.+\\})\\);");
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

                    int generalCount = productCommentsCount.getInt("generalCount");
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
                    commentContent.setItem(item);
                    String content = cmt.getString("content");
                    commentContent.setContent(content);

//                    logger.info("comment_id     ->" + commentContent.getFeatureId() + "\n" +
//                                "content        ->" + commentContent.getContent() + "\n" +
//                                "is_good        ->" + commentContent.getGood() + "\n" +
//                                "content_id     ->" + commentContent.getCommentId() + "\n" +
//                                "item_id        ->" + commentContent.getItemId());
                    commentContent.setCrawDate(LocalDate.now());
                    commentService.insertByComment(commentContent);
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
        item.setCrawDate(LocalDate.now());
        itemService.update(item);
    }
}
