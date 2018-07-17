package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.*;
import cn.kastner.analyst.exception.CrawlerException;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    JdItemService jdItemService;

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
    public JdCrawlerServiceImpl(JdItemService jdItemService, CommentService commentService, PriceService priceService, BrandService brandService, CategoryService categoryService, MarketRepository marketRepository, PhoneDetailService phoneDetailService) {
        this.jdItemService = jdItemService;
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

    private Finder finder = new Finder();

    private Lang lang = new Lang();

    private JdItem jdItem = new JdItem();

    private Document doc;

    /**
     * 匹配商品主页面
     * @param url 商品主页面链接
     * @return 商品页面对象
     * @throws IOException 链接失败
     */
    private Document getMainDoc(String url) throws IOException {
        doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .get();
        doc.getElementsByClass("Ptable-tips").remove();
        return doc;
    }

    /**
     * 匹配商品电商
     * @return 电商
     */
    private Market getMarket() {
        return marketRepository.findByCode(Market.Code.JD);
    }

    /**
     * 匹配商品 SkuId
     * @return 商品 SkuId
     * @throws CrawlerException 未匹配到商品 SkuId
     */
    private String getSkuId() throws CrawlerException {
        String skuId = finder.getString(doc.head().toString(), "skuId: (\\d*),", 1);
        if (skuId != null) {
            return skuId;
        } else {
            throw new CrawlerException("未匹配到 skuId");
        }
    }

    /**
     * 匹配商品描述
     * @return 商品描述
     * @throws CrawlerException 未匹配到商品描述
     */
    private String getDescription() throws CrawlerException {
        Elements itemSelect = doc.getElementsByClass("jdItem  selected  ");
        String description = itemSelect.get(0).getElementsByTag("i").get(0).text();
        if (description != null) {
            return description;
        } else {
            throw new CrawlerException("未匹配到描述");
        }
    }

    /**
     * 匹配商品品牌
     * @return 商品品牌
     * @throws CrawlerException 未匹配到商品品牌
     */
    private Brand getBrand() throws CrawlerException {
        String brandStr = doc.getElementById("parameter-brand")
                .getElementsByTag("a")
                .get(0)
                .text();
        if (brandStr != null) {
            String brandZhName = finder.getString(brandStr, "(.*)（.*）", 1);
            String brandEnName = finder.getString(brandStr, ".*（(.*)）", 1);
            if (brandEnName == null || brandZhName == null) {
                if (lang.hasChinese(brandStr)) {
                    brandZhName = brandStr;
                    Brand brandDb = brandService.findByZhName(brandZhName);
                    if (brandDb == null) {
                        Brand brand = new Brand();
                        brand.setBrandZhName(brandZhName);
                        brandService.insertByBrand(brand);
                        return brand;
                    } else {
                        return brandDb;
                    }
                } else {
                    brandEnName = brandStr;
                    Brand brandDb = brandService.findByEnName(brandEnName.toUpperCase());
                    if (brandDb == null) {
                        Brand brand = new Brand();
                        brand.setBrandEnName(brandEnName.toUpperCase());
                        brandService.insertByBrand(brand);
                        return brand;
                    } else {
                        return brandDb;
                    }
                }
            } else {
                Brand brandDb = brandService.findByEnOrZhName(brandEnName.toUpperCase(), brandZhName);
                if (brandDb == null) {
                    Brand brand = new Brand();
                    brand.setBrandZhName(brandZhName);
                    brand.setBrandEnName(brandEnName.toUpperCase());
                    brandService.insertByBrand(brand);
                    return brand;
                } else {
                    return brandDb;
                }
            }
        } else {
            throw new CrawlerException("未匹配到品牌");
        }
    }

    /**
     * 匹配商品中文名称
     * @return 中文名称
     * @throws CrawlerException 未匹配
     */
    private String getZhName() throws CrawlerException {
        String zhName = finder.getString(doc.title(), "(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        if (zhName != null) {
            logger.info("Get Item zhName from title: " + zhName);
            return zhName;
        } else {
            throw new CrawlerException("未匹配到中文名");
        }
    }

    /**
     * 匹配商品型号
     * @return 商品型号
     * @throws CrawlerException 未匹配
     */
    private String getModel() throws CrawlerException {
        Element dlEl = doc
                // `div.Ptable`
                .getElementsByClass("Ptable").get(0)
                // div.Ptable-jdItem
                .getElementsByClass("Ptable-jdItem").get(0)
                // dl
                .getElementsByTag("dl").get(0);
        Elements dtEl = dlEl.getElementsByTag("dt");
        Elements ddEl = dlEl.getElementsByTag("dd");

        int index = 0;
        for(Element e: dtEl) {
            if (e.text().equals("型号")){
                String model = ddEl.get(index).text();
                return lang.removeChinese(model);
            }
            index++;
        }
        throw new CrawlerException("未匹配到型号");
    }

    /**
     * 匹配商品店铺Id
     * @return 店铺Id
     * @throws CrawlerException 未匹配
     */
    private String getVendorId() throws CrawlerException {
        Pattern vendorIdPattern = Pattern.compile("venderId:(\\d+),");
        Matcher vendorIdMatcher = vendorIdPattern.matcher(doc.head().toString());
        if (vendorIdMatcher.find()) {
            return vendorIdMatcher.group(1);
        } else {
            throw new CrawlerException("未匹配到店铺Id");
        }
    }

    /**
     * 匹配商品图片链接列表
     * @return 图片链接列表以逗号分隔
     * @throws CrawlerException 未匹配
     */
    private String getImageGroup() throws CrawlerException {
        String imageListStr = finder.getString(doc.head().toString(), "imageList: \\[\\\"(.+)\\\"\\]", 1);
        if (imageListStr != null) {
            return StringUtils.join(imageListStr.split("\",\""), ",");
        } else {
            throw new CrawlerException("未匹配到展示图片列表");
        }
    }

    /**
     * 获取是否自营
     * @return 是否自营
     */
    private Boolean getIsSelfSell() {
        Element selfSelling = doc.getElementsByClass("u-jd").get(0);
        return selfSelling != null;
    }

    /**
     * 匹配商品 Rom
     * @return Rom
     */
    private Double crawlRom() {
        Double rom = Double.parseDouble(finder.getString(doc.toString(), "机身内存：(.*)GB</li>", 1));
        jdItemService.insertByItem(jdItem);
        List<PhoneDetail> phoneDetailList = phoneDetailService.findByItemAndRom(jdItem, rom);
        if (phoneDetailList.isEmpty()) {
            PhoneDetail phoneDetail = new PhoneDetail();
            phoneDetail.setItem(jdItem);
            phoneDetail.setRomCapacity(rom);
            phoneDetailService.insertByPhoneDetail(phoneDetail);
        }
        return rom;
    }

    /**
     * 匹配商品 CommentVersion
     * @return CommentVersion
     * @throws CrawlerException 未匹配
     */
    private String getCommentVersion() throws CrawlerException {
        Pattern commentVersionPattern = Pattern.compile("commentVersion:\\'(.+)\\',");
        Matcher commentVersionMatcher = commentVersionPattern.matcher(doc.head().toString());
        if (commentVersionMatcher.find()) {
            return commentVersionMatcher.group(1);
        } else {
            throw new CrawlerException("未匹配到 CommentVersion");
        }
    }

    /**
     * 匹配商品店铺
     * @return 商品店铺
     * @throws CrawlerException 未匹配
     */
    private String getVendor() throws CrawlerException {
        String vendor = finder.getString(doc.toString(), "dianpuname.*\">(.*)</a>", 1);
        if (vendor != null) {
            return vendor;
        } else {
            throw new CrawlerException("未匹配到店铺");
        }
    }

    /**
     * 匹配商品品类
     * @return 品类
     * @throws CrawlerException 未匹配到品类
     */
    private Category getCategory() throws CrawlerException {
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
                return categoryDb;
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
            return category;
        } else {
            throw new CrawlerException("未匹配到品类");
        }
    }


//    private void getCommentCount() {
//        // get commentsCount
//        Document commentsCountDoc = Jsoup.connect("http://club.jd.com/comment/productCommentSummaries.action")
//                .header("accept", "*/*")
//                .header("accept-encoding", "gzip, deflate, br")
//                .header("accept-language", "zh-CN,zh;q=0.9")
//                .header("referer", "https://item.jd.com/" + jdItem.getSkuId() + ".html")
//                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
//                        "AppleWebKit/537.36 (KHTML, like Gecko)" +
//                        "Chrome/65.0.3325.181 Safari/537.36")
//
//                .data("referenceIds", jdItem.getSkuId())
//                .data("callback", "jQuery" + 6546654)
//                .data("_", "" + System.currentTimeMillis() / 1000)
//                .get();
//        Pattern commentsCountPattern = Pattern.compile("jQuery" + 6546654 + "\\(\\{\\\"CommentsCount\\\":\\[(.+)]\\}\\);");
//        Matcher commentsCountMatcher = commentsCountPattern.matcher(commentsCountDoc.toString());
//        if (commentsCountMatcher.find()) {
//
//            JSONObject commentsCount = new JSONObject(commentsCountMatcher.group(1));
//
//            int generalCount = commentsCount.getInt("GeneralCount");
//            jdItem.setGeneralCount(generalCount);
//
//            String generalCountStr = commentsCount.getString("GeneralCountStr");
//            jdItem.setGeneralCountStr(generalCountStr);
//
//            int goodCount = commentsCount.getInt("GoodCount");
//            jdItem.setGoodCount(goodCount);
//
//            String goodCountStr = commentsCount.getString("GoodCountStr");
//            jdItem.setGoodCountStr(goodCountStr);
//
//            int poorCount = commentsCount.getInt("PoorCount");
//            jdItem.setPoorCount(poorCount);
//
//            String poorCountStr = commentsCount.getString("PoorCountStr");
//            jdItem.setGoodCountStr(poorCountStr);
//        }
//    }

    /**
     * 获取库存信息
     * @return 库存信息JSON对象
     * @throws IOException 连接失败
     */
    private JSONObject getStockJSON() throws IOException {
        Document stockDoc = Jsoup.connect("https://c0.3.cn/stock?" + "skuId=" + jdItem.getSkuId() +
                "&area=1_72_2799_0" +
                "&vendorId=" + jdItem.getVendorId() +
                "&cat=" + jdItem.getCategory().getCategoryStr() +
                "&buyNum=1" +
                "&choseSuitSkuIds=" +
                "&extraParam={\"originid\":\"1\"}" +
                "&ch=1" +
                "&pduid=" + (System.currentTimeMillis() / 1000) + 1901035936 +
                "&pdpin=" +
                "&detailedAdd=null" +
                "&callback=jQuery" + 6546654)
                .get();
        Pattern stockPattern = Pattern.compile("jQuery" + 6546654 + "\\(\\{\\\"stock\\\":(\\{.+\\}),\\\"");
        Matcher stockMatcher = stockPattern.matcher(stockDoc.toString());
        if (stockMatcher.find()) {
            return new JSONObject(stockMatcher.group(1));
        } else {
            throw new IOException("获取库存信息失败");
        }
    }

    private Price getPrice() throws IOException {
        JSONObject stock = getStockJSON();
        JSONObject jdPrice = stock.getJSONObject("jdPrice");
        String p = jdPrice.getString("p");
        // has plus price
        Price price = new Price();
        price.setPrice(Double.parseDouble(p));
        price.setItem(jdItem);
        price.setMarket(getMarket());
        price.setCrawDateTime(LocalDateTime.now());
        priceService.insertByPrice(price);
        return price;
    }

    /**
     * 基础信息爬取
     * @param url 商品链接
     * @return 商品
     */
    @Override
    public Item crawl(String url) throws Exception {

        doc = getMainDoc(url);

        jdItem.setMarket(getMarket());
        jdItem.setSkuId(getSkuId());
        jdItem.setDescription(getDescription());
        jdItem.setBrand(getBrand());
        jdItem.setZhName(getZhName());
        jdItem.setModel(getModel());
        jdItem.setVendor(getVendor());
        jdItem.setVendorId(getVendorId());
        jdItem.setImageGroup(getImageGroup());
        jdItem.setSelfSell(getIsSelfSell());
        jdItem.setCommentVersion(getCommentVersion());
        jdItem.setCategory(getCategory());

        getPrice();





        jdJdItemService.insertByJdItem(jdItem);
        logger.info("jdItem has been saved.");

        return jdItem;
    }

    /**
     * 商品评论爬取
     */
    @Override
    public void crawItemComment () throws Exception {
        // get comments
        String commentStr = "";
        for (int currentPage = 0; currentPage < 5; currentPage++) {
            String commentUrl = "https://club.jd.com/comment/skuProductPageComments.action?" +
                    "callback=fetchJSON_comment98vv" + jdItem.getCommentVersion() +
                    "&productId=" + jdItem.getSkuId() +
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
                    .header("referer", "https://jdItem.jd.com/" + jdItem.getSkuId() + ".html")
                    .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .execute();
            commentStr = commentDoc.body();
            logger.info(commentStr);
            Pattern commentPattern = Pattern.compile("fetchJSON_comment98vv" + jdItem.getCommentVersion() + "\\((\\{.+\\})\\);");
            Matcher commentMatcher = commentPattern.matcher(commentStr);
            if (commentMatcher.find()) {
                JSONObject comment = new JSONObject(commentMatcher.group(1));
                if (0 == currentPage) {
                    JSONObject productCommentsCount = comment.getJSONObject("productCommentSummary");

                    int commentCount = productCommentsCount.getInt("commentCount");
                    jdItem.setCommentCount(commentCount);

                    String commentCountStr = productCommentsCount.getString("commentCountStr");
                    jdItem.setCommentCountStr(commentCountStr);

                    int generalCount = productCommentsCount.getInt("generalCount");
                    jdItem.setGeneralCount(generalCount);

                    String generalCountStr = productCommentsCount.getString("generalCountStr");
                    jdItem.setGeneralCountStr(generalCountStr);

                    int goodCount = productCommentsCount.getInt("goodCount");
                    jdItem.setGoodCount(goodCount);

                    String goodCountStr = productCommentsCount.getString("goodCountStr");
                    jdItem.setGoodCountStr(goodCountStr);


                    int poorCount = productCommentsCount.getInt("poorCount");
                    jdItem.setPoorCount(poorCount);

                    String poorCountStr = productCommentsCount.getString("poorCountStr");
                    jdItem.setPoorCountStr(poorCountStr);

                }
                JSONArray comments = comment.getJSONArray("comments");
                for (int i = 0; i < comments.length(); i++) {
                    JSONObject cmt = comments.getJSONObject(i);
                    Comment commentContent = new Comment();
                    commentContent.setItem(jdItem);
                    String content = cmt.getString("content");
                    commentContent.setContent(content);
                    commentContent.setCrawDate(LocalDate.now());
                    commentService.insertByComment(commentContent);
                    logger.info("commentContent has been saved.");
                }
            }
            TimeUnit.SECONDS.sleep(3);

        }
        jdItem.setCrawDate(LocalDate.now());
        jdJdItemService.update(jdItem);
    }
}
