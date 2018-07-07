package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.*;
import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.repository.core.MarketRepository;
import cn.kastner.analyst.service.core.*;
import cn.kastner.analyst.service.crawler.JdCrawlerService;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import cn.kastner.analyst.util.crawler.Lang;
import cn.kastner.analyst.util.crawler.Finder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.thymeleaf.util.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 京东爬虫服务实现
 */
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

    private final MarketRepository marketRepository;

    private final PhoneDetailService phoneDetailService;

    @Autowired
    public JdCrawlerServiceImpl(ItemService itemService, CommentService commentService, PriceService priceService, BrandService brandService, CategoryService categoryService, MarketRepository marketRepository, PhoneDetailService phoneDetailService) {
        this.itemService = itemService;
        this.commentService = commentService;
        this.priceService = priceService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.marketRepository = marketRepository;
        this.phoneDetailService = phoneDetailService;
    }


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 基础信息爬取
     * @param url 商品链接
     * @return 商品
     */
    @Override
    public Item crawItemByUrl(String url) throws Exception {

        Finder finder = new Finder();
        Lang lang = new Lang();
        Item item = new Item();
        Market market = marketRepository.findByCode(Market.Code.JD);
        item.setMarket(market);

        logger.info("Connecting to base url ...");
        Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                    .get();

        doc.getElementsByClass("Ptable-tips").remove();

        // get skuId
        String skuid = finder.getString(doc.head().toString(), "skuid: (\\d*),", 1);
        if (skuid == null) {
            logger.warning("can't get skuid");
        } else {
            item.setSkuId(skuid);
            logger.info(MessageFormat.format("Get skuid from head: {0}", skuid));
        }

        // check if has item already
        Item itemDb = itemService.findBySkuId(item.getSkuId());
        if (null != itemDb) {
            return itemDb;
        }

        // get select color
        Elements itemSelect = doc.getElementsByClass("item  selected  ");
        String color = itemSelect.get(0).getElementsByTag("i").get(0).text();
        if (color != null) {
            item.setColor(color);
        }

        // get brand
        String brandStr = doc.getElementById("parameter-brand")
                .getElementsByTag("a")
                .get(0)
                .text();
        if (brandStr == null) {
            logger.warning("no brand");
        } else {
            String brandZhName = finder.getString(brandStr, "(.*)（.*）", 1);
            String brandEnName = finder.getString(brandStr, ".*（(.*)）", 1);
            if (brandEnName == null || brandZhName == null) {
                if (lang.hasChinese(brandStr)) {
                    Brand brandDb = brandService.findByZhName(brandStr);
                    if (brandDb == null) {
                        Brand brand = new Brand();
                        brand.setBrandZhName(brandStr);
                        brandService.insertByBrand(brand);
                        item.setBrand(brand);
                    } else {
                        item.setBrand(brandDb);
                    }
                } else {
                    Brand brandDb = brandService.findByEnName(brandStr.toUpperCase());
                    if (brandDb == null) {
                        Brand brand = new Brand();
                        brand.setBrandEnName(brandStr.toUpperCase());
                        brandService.insertByBrand(brand);
                        item.setBrand(brand);
                    } else {
                        item.setBrand(brandDb);
                    }
                }
            } else {
                Brand brandDb = brandService.findByEnOrZhName(brandEnName.toUpperCase(), brandZhName);
                if (brandDb == null) {
                    Brand brand = new Brand();
                    brand.setBrandZhName(brandZhName);
                    brand.setBrandEnName(brandEnName.toUpperCase());
                    brandService.insertByBrand(brand);
                    item.setBrand(brand);
                } else {
                    item.setBrand(brandDb);
                }
            }
        }

        // get itemCname from html title
        String zhName = finder.getString(doc.title(), "(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        if (zhName == null) {
            logger.warning("Can't get item zhName");
        } else {
            item.setZhName(zhName);
            logger.info("Get Item zhName from title: " + zhName);
        }

        // 获取商品型号（model）
        Element dlEl = doc
                // div.Ptable
                .getElementsByClass("Ptable").get(0)
                // div.Ptable-item
                .getElementsByClass("Ptable-item").get(0)
                // dl
                .getElementsByTag("dl").get(0);
        Elements dtEl = dlEl.getElementsByTag("dt");
        Elements ddEl = dlEl.getElementsByTag("dd");

        int index = 0;
        for(Element e: dtEl) {
            if (e.text().equals("型号")){
                String model = ddEl.get(index).text();
                System.out.println("getModel    ->" + lang.removeChinese(model));
                item.setModel(lang.removeChinese(model));
            }
            index++;
        }



        Long itemId = item.getId();





        // analyse doc


        // get vendorId
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

        String imageListStr = finder.getString(doc.head().toString(), "imageList: \\[\\\"(.+)\\\"\\]", 1);
        if (imageListStr == null) {
            logger.warning("no imageList");
        } else {
            String imageList = StringUtils.join(
                    imageListStr.split("\",\""), ","
            ) ;
            item.setImageGroup(imageList);
            logger.info("Get  imageList from head: " + imageList);
        }

        // check if self selling
        Element selfSelling = doc.getElementsByClass("u-jd").get(0);
        if (selfSelling == null) {
            item.setIsSelfSell(false);
        } else {
            item.setIsSelfSell(true);
        }

        // 获取 ROM
        Double rom = Double.parseDouble(finder.getString(doc.toString(), "机身内存：(.*)GB</li>", 1));
        PhoneDetail phoneDetail = new PhoneDetail();
        itemService.insertByItem(item);
        phoneDetail.setItem(item);
        phoneDetail.setRomCapacity(rom);
        phoneDetailService.insertByPhoneDetail(phoneDetail);

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

        // get vendor
        String vendor = finder.getString(doc.toString(), "dianpuname.*\">(.*)</a>", 1);
        if (vendor == null) {
            logger.warning("no vendor");
        } else {
            item.setVendor(vendor);
            logger.info("Get vendor: " + vendor);
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


            // get level1 name
            Pattern level1Pattern = Pattern.compile("mbNav-1\">(.*)</a>");
            Matcher level1Matcher = level1Pattern.matcher(doc.toString());
            if (level1Matcher.find()) {
                String level1 = level1Matcher.group(1);
                category.setLevelOneName(level1);
            }

            // get level2 name
            Pattern level2Pattern = Pattern.compile("mbNav-2\">(.*)</a>");
            Matcher level2Matcher = level2Pattern.matcher(doc.toString());
            if (level2Matcher.find()) {
                String level2 = level2Matcher.group(1);
                category.setLevelTwoName(level2);
            }

            // get level3 name
            Pattern level3Pattern = Pattern.compile("mbNav-3\">(.*)</a>");
            Matcher level3Matcher = level3Pattern.matcher(doc.toString());
            if (level3Matcher.find()) {
                String level3 = level3Matcher.group(1);
                category.setLevelThreeName(level3);
            }


            item.setCategory(category);
            logger.info("Get cat from head: " + categoryStr);
        } else {
            logger.warning("no category");
        }

        Integer jQueryId = 6546654;


        // get commentsCount
        logger.info("Connecting to http://club.jd.com/comment/productCommentSummaries.action");
        Document commentsCountDoc = Jsoup.connect("http://club.jd.com/comment/productCommentSummaries.action")
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

        Pattern stockPattern = Pattern.compile("jQuery" + jQueryId + "\\(\\{\\\"stock\\\":(\\{.+\\}),\\\"");
        Matcher stockMatcher = stockPattern.matcher(stockStr);
        if (stockMatcher.find()) {

            JSONObject stock = new JSONObject(stockMatcher.group(1));

//            JSONObject selfDeliver;
//            try {
//                selfDeliver = stock.getJSONObject("self_D");
//                String vender = selfDeliver.getString("vender");
//                item.setVendor(vender);
//            } catch (Exception e) {
//                logger.info("get selfDeliver from D");
//            }
//
//            try {
//                selfDeliver = stock.getJSONObject("D");
//                String vender = selfDeliver.getString("vender");
//                item.setVendor(vender);
//            } catch (Exception e) {
//                logger.info("get selfDeliver from self_D");
//            }


            JSONObject jdPrice = stock.getJSONObject("jdPrice");
            String p = jdPrice.getString("p");
            // has plus price
            Price price = new Price();
            price.setPrice(Double.parseDouble(p));
            price.setItem(item);
            price.setMarket(market);

            Long volume = (long) generalCount;
            price.setVolume(volume);


            price.setCrawDateTime(LocalDateTime.now());
            logger.info("price_id   ->" + price.getPriceId() + "\n" +
                    "date       ->" + price.getCrawDateTime() + "\n" +
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
     * 商品评论爬取
     * @param item 商品
     */
    @Override
    public void crawItemComment (Item item) throws Exception {
        // get comments
        String commentStr = "";
        for (int currentPage = 0; currentPage < 5; currentPage++) {
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
//                                "item_id        ->" + commentContent.getId());
                    commentContent.setCrawDate(LocalDate.now());
                    commentService.insertByComment(commentContent);
                    logger.info("commentContent has been saved.");
                }
            }
            TimeUnit.SECONDS.sleep(3);

        }
        logger.info("[check]commentCountStr    ->" + item.getCommentCountStr());
        logger.info("[check]generalCountStr    ->" + item.getGeneralCountStr());
        logger.info("[check]goodCountStr       ->" + item.getGoodCountStr());
        logger.info("[check]poorCountStr       ->" + item.getPoorCountStr());
        item.setCrawDate(LocalDate.now());
        itemService.update(item);
    }
}
