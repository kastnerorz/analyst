package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.service.core.BrandService;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import cn.kastner.analyst.util.crawler.Finder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * PhoneDB 爬虫服务实现
 */
public class PhoneDbCrawlerServiceImpl implements PhoneDbCrawlerService {

    private static String strClassName = PhoneDbCrawlerServiceImpl.class.getName();
    private static Logger logger = Logger.getLogger(strClassName);

    private Item item;

    @Autowired
    private final BrandService brandService;
    private final Finder finder;

    public PhoneDbCrawlerServiceImpl(BrandService brandService, Finder finder) {
        this.brandService = brandService;
        this.finder = finder;
    }


    /**
     * PhoneDB 爬虫主函数
     * @param item 商品对象
     * @return Item 商品对象
     */
    @Override
    public Item crawByItem(Item item) {
        this.item = item;
        String url = searchByModelAndRom(item.getModel(), item.getRom());
        crawDetails(url);
        return item;
    }

    /**
     * @param model 型号
     * @param rom   存储空间
     * @return PhoneDB详情链接
     */
    @Override
    public String searchByModelAndRom(String model, String rom) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Upgrade-Insecure-Requests", "1");
        Document doc = new Document("http://phonedb.net");
        try {
            doc = Jsoup.connect("/index.php?m=device&s=query")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/66.0.3359.181 Safari/537.36")
                    .headers(headers)
                    .post();
        } catch (IOException e) {
            logger.warning(e.toString());
        }

        Element resultEl = doc.getElementsByAttributeValue("title", "See detailed datasheet")
                            .get(0);
        return resultEl.attr("title");
    }

    /**
     * @param url PhoneDB详情链接
     */
    @Override
    public void crawDetails(String url) {
        HashMap<String , String> headers = new HashMap<>();
        Document doc = new Document("http://phonedb.net");
        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/66.0.3359.181 Safari/537.36")
                    .headers(headers)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element dataTableEl = doc.getElementsByTag("table").get(0);
        Elements dataTrEls = dataTableEl.getElementsByTag("tr");

        PhoneDetail phoneDetail = new PhoneDetail();
        phoneDetail.setItem(item);
        phoneDetail.setCrawDate(LocalDate.now());
        // TODO RamClock
        for(Element el: dataTrEls) {
            Elements dataTdEls = el.getElementsByTag("td");
            String attr = dataTdEls.text();
            String val = dataTableEl.text();

            if (attr.equals("Brand")) {
                Brand brandDb = brandService.findByEnName(attr);
                if (brandDb == null) {
                    Brand brand = new Brand();
                    brand.setBrandEnName(val);
                    phoneDetail.setBrand(brand);
                } else {
                    phoneDetail.setBrand(brandDb);
                }
            } else if (attr.equals("Model")) {
                phoneDetail.setModel(val);
            } else if (attr.equals("Released")) {
//                phoneDetail.setReleased(val);
                // TODO handle date (year, month)
            } else if (attr.equals("Additional Features")){
                phoneDetail.setAddlFeatures(val);
            } else if (attr.equals("Mobile Operator")) {
                phoneDetail.setOperator(val);
            } else if (attr.equals("Width")) {
                phoneDetail.setWidth(finder.getDouble(val, 1));
            } else if (attr.equals("Height")) {
                phoneDetail.setHeight(finder.getDouble(val, 1));
            } else if (attr.equals("Depth")) {
                phoneDetail.setDepth(finder.getDouble(val, 1));
            } else if (attr.equals("Mass")) {
                phoneDetail.setMass(finder.getDouble(val, 1));
            } else if (attr.equals("Platform")) {
                phoneDetail.setPlatform(val);
            } else if (attr.equals("Operating System")) {
                phoneDetail.setOs(val);
            } else if (attr.equals("Software Extras")) {
                phoneDetail.setSwExtras(val);
            } else if (attr.equals("CPU Clock")) {
                phoneDetail.setCpuClock(finder.getDouble(val, 1));
            } else if (attr.equals("RAM Type")) {
                phoneDetail.setRamType(val);
            } else if (attr.equals("RAM Capacity")) {
                phoneDetail.setRamCapacity(finder.getDouble(val, 1));
            } else if (attr.equals("Non-volatile Memory Type")) {
                phoneDetail.setRomType(val);
            }
        }
    }
}
