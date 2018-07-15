package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.*;
import cn.kastner.analyst.domain.crawler.JdItem;
import cn.kastner.analyst.domain.detail.PhoneDetail;
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

import javax.sql.rowset.CachedRowSet;
import java.io.IOException;
import java.security.cert.CertificateRevokedException;
import java.text.MessageFormat;
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

    private Finder finder = new Finder();

    private Lang lang = new Lang();

    private JdItem jdItem = new JdItem();

    private Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .get();
    }

    private Document document;

    private Market getMarket() {
        return marketRepository.findByCode(Market.Code.JD);
    }

    private void processDocument() {
        document.getElementsByClass("Ptable-tips").remove();
    }

    private String getSkuId() throws CrawlerException {
        String skuId = finder.getString(document.head().toString(), "skuId: (\\d*),", 1);
        if (skuId != null) {
            return skuId;
        } else {
            throw new CrawlerException("未获取到 skuId");
        }
    }

    private String getDescription() throws CrawlerException {
        Elements itemSelect = document.getElementsByClass("jdItem  selected  ");
        String description = itemSelect.get(0).getElementsByTag("i").get(0).text();
        if (description != null) {
            return description;
        } else {
            throw new CrawlerException("未获取到描述");
        }
    }

    private Brand getBrand() throws CrawlerException {
        String brandStr = document.getElementById("parameter-brand")
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
            throw new CrawlerException("未获取到品牌");
        }
    }

    private String getZhName() throws CrawlerException {
        String zhName = finder.getString(document.title(), "(?<=\\\\[)(\\\\S+)(?=\\\\])|(?<=【)[^】]*");
        if (zhName != null) {
            logger.info("Get Item zhName from title: " + zhName);
            return zhName;
        } else {
            throw new CrawlerException("未获取到中文名");
        }
    }

    private String getModel() throws CrawlerException {
        Element dlEl = document
                // div.Ptable
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
        throw new CrawlerException("未获取到型号");
    }

    private String getVendorId() throws CrawlerException {
        Pattern vendorIdPattern = Pattern.compile("venderId:(\\d+),");
        Matcher vendorIdMatcher = vendorIdPattern.matcher(document.head().toString());
        if (vendorIdMatcher.find()) {
            String vendorId = vendorIdMatcher.group(1);
            logger.info("Get  vendorId from head: " + vendorId);
            return vendorId;
        } else {
            throw new CrawlerException("未获取到店铺Id");
        }
    }

    private String getImageGroup() throws CrawlerException {
        String imageListStr = finder.getString(document.head().toString(), "imageList: \\[\\\"(.+)\\\"\\]", 1);
        if (imageListStr != null) {
            return StringUtils.join(imageListStr.split("\",\""), ",");
        } else {
            throw new CrawlerException("未获取到展示图片列表");
        }
    }

    private Boolean getIsSelfSell() {
        Element selfSelling = document.getElementsByClass("u-jd").get(0);
        return selfSelling != null;
    }

    private Double crawlRom() {
        Double rom = Double.parseDouble(finder.getString(document.toString(), "机身内存：(.*)GB</li>", 1));
        itemService.insertByItem(jdItem);
        List<PhoneDetail> phoneDetailList = phoneDetailService.findByItemAndRom(jdItem, rom);
        if (phoneDetailList.isEmpty()) {
            PhoneDetail phoneDetail = new PhoneDetail();
            phoneDetail.setItem(jdItem);
            phoneDetail.setRomCapacity(rom);
            phoneDetailService.insertByPhoneDetail(phoneDetail);
        }
        return rom;
    }

    private String getCommentVersion() throws CrawlerException {
        Pattern commentVersionPattern = Pattern.compile("commentVersion:\\'(.+)\\',");
        Matcher commentVersionMatcher = commentVersionPattern.matcher(document.head().toString());
        if (commentVersionMatcher.find()) {
            String commentVersion = commentVersionMatcher.group(1);
            logger.info("Get comment version from head: " + commentVersion);
            return commentVersion;
        } else {
            throw new CrawlerException("未获取到 CommentVersion");
        }
    }

    private String getVendor() throws CrawlerException {
        String vendor = finder.getString(document.toString(), "dianpuname.*\">(.*)</a>", 1);
        if (vendor != null) {
            return vendor;
        } else {
            throw new CrawlerException("未获取到店铺");
        }
    }

    /**
     * 获取商品品类
     * @return 品类
     * @throws CrawlerException 未获取到品类
     */
    private Category getCategory() throws CrawlerException {
        Pattern catPattern = Pattern.compile("cat: \\[(.+)\\],");
        Matcher catMatcher = catPattern.matcher(document.head().toString());
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
            Matcher level1Matcher = level1Pattern.matcher(document.toString());
            if (level1Matcher.find()) {
                String level1 = level1Matcher.group(1);
                category.setLevelOneName(level1);
            }

            // get level2 name
            Pattern level2Pattern = Pattern.compile("mbNav-2\">(.*)</a>");
            Matcher level2Matcher = level2Pattern.matcher(document.toString());
            if (level2Matcher.find()) {
                String level2 = level2Matcher.group(1);
                category.setLevelTwoName(level2);
            }

            // get level3 name
            Pattern level3Pattern = Pattern.compile("mbNav-3\">(.*)</a>");
            Matcher level3Matcher = level3Pattern.matcher(document.toString());
            if (level3Matcher.find()) {
                String level3 = level3Matcher.group(1);
                category.setLevelThreeName(level3);
            }
            logger.info("Get cat from head: " + categoryStr);
            return category;
        } else {
            throw new CrawlerException("未获取到品类");
        }
    }


    private void getCommentCount() {
        // get commentsCount
        Document commentsCountDoc = Jsoup.connect("http://club.jd.com/comment/productCommentSummaries.action")
                .header("accept", "*/*")
                .header("accept-encoding", "gzip, deflate, br")
                .header("accept-language", "zh-CN,zh;q=0.9")
                .header("referer", "https://item.jd.com/" + jdItem.getSkuId() + ".html")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                        "AppleWebKit/537.36 (KHTML, like Gecko)" +
                        "Chrome/65.0.3325.181 Safari/537.36")

                .data("referenceIds", jdItem.getSkuId())
                .data("callback", "jQuery" + 6546654)
                .data("_", "" + System.currentTimeMillis() / 1000)
                .get();

         commentsCount;
        Pattern commentsCountPattern = Pattern.compile("jQuery" + 6546654 + "\\(\\{\\\"CommentsCount\\\":\\[(.+)]\\}\\);");
        Matcher commentsCountMatcher = commentsCountPattern.matcher(commentsCountDoc.toString());
        if (commentsCountMatcher.find()) {

            JSONObject commentsCount = new JSONObject(commentsCountMatcher.group(1));

            int generalCount = commentsCount.getInt("GeneralCount");
            jdItem.setGeneralCount(generalCount);

            String generalCountStr = commentsCount.getString("GeneralCountStr");
            jdItem.setGeneralCountStr(generalCountStr);

            int goodCount = commentsCount.getInt("GoodCount");
            jdItem.setGoodCount(goodCount);

            String goodCountStr = commentsCount.getString("GoodCountStr");
            jdItem.setGoodCountStr(goodCountStr);

            int poorCount = commentsCount.getInt("PoorCount");
            jdItem.setPoorCount(poorCount);

            String poorCountStr = commentsCount.getString("PoorCountStr");
            jdItem.setGoodCountStr(poorCountStr);
        }
    }
    /**
     * 基础信息爬取
     * @param url 商品链接
     * @return 商品
     */
    @Override
    public Item crawl(String url) throws Exception {

        document = getDocument(url);
        processDocument();

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


        Integer jQueryId = 6546654;



        jQueryId = jQueryId + 1;


        // get stock info
        Connection.Response stockDoc = Jsoup.connect("https://c0.3.cn/stock?" + "skuId=" + jdItem.getSkuId() +
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
                "&callback=jQuery" + jQueryId)
                .ignoreContentType(true)
                .execute();
        String stockStr = stockDoc.body();
        logger.info(stockStr);
        Pattern stockPattern = Pattern.compile("jQuery" + jQueryId + "\\(\\{\\\"stock\\\":(\\{.+\\}),\\\"");
        Matcher stockMatcher = stockPattern.matcher(stockStr);
        if (stockMatcher.find()) {

            JSONObject stock = new JSONObject(stockMatcher.group(1));

            JSONObject jdPrice = stock.getJSONObject("jdPrice");
            String p = jdPrice.getString("p");
            // has plus price
            Price price = new Price();
            price.setPrice(Double.parseDouble(p));
            price.setItem(jdItem);
            price.setMarket(getMarket());

            Long volume = (long) generalCount;
            price.setVolume(volume);

            price.setCrawDateTime(LocalDateTime.now());
            priceService.insertByPrice(price);
            logger.info("price has been saved.");
        }


        itemService.insertByItem(jdItem);
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
        jdItemService.update(jdItem);
    }
}
