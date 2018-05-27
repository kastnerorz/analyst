package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.service.core.BrandService;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import cn.kastner.analyst.service.detail.PhoneDetailService;
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
    private final PhoneDetailService phoneDetailService;

    public PhoneDbCrawlerServiceImpl(BrandService brandService, Finder finder, PhoneDetailService phoneDetailService) {
        this.brandService = brandService;
        this.finder = finder;
        this.phoneDetailService = phoneDetailService;
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
        int index = 0;
        for(Element el: dataTrEls) {
            index++;
            Elements dataTdEls = el.getElementsByTag("td");
            String attr = dataTdEls.get(0).text();
            String val = dataTdEls.get(1).text();

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
                Element ramClkTrEl = dataTrEls.get(index + 1);
                Elements ramClkTdEls = ramClkTrEl.getElementsByTag("td");
                phoneDetail.setRamCapacity(finder.getDouble(ramClkTdEls.get(1).text(), 1));
            } else if (attr.equals("RAM Capacity")) {
                phoneDetail.setRamCapacity(finder.getDouble(val, 1));
            } else if (attr.equals("Non-volatile Memory Type")) {
                phoneDetail.setRomType(val);
            } else if (attr.equals("Non-volatile Memory Capacity")) {
                phoneDetail.setRomCapacity(finder.getDouble(val, 1));
            } else if (attr.equals("Display Resolution")) {
                phoneDetail.setWideRez(finder.getDouble(val, 1).intValue());
                phoneDetail.setLongRez(finder.getDouble(val, 2).intValue());
            } else if (attr.equals("Display Diagonal")) {
                Element dispDiagonalTrEl = dataTrEls.get(index + 1);
                Elements dispDiagonalTdEls = dispDiagonalTrEl.getElementsByTag("td");
                phoneDetail.setDispDiagonal(finder.getDouble(dispDiagonalTdEls.get(1).text(), 1));
            } else if (attr.equals("Display Area Utilization")) {
                phoneDetail.setDispAreaUtilization(finder.getDouble(val, 1).intValue());
            } else if (attr.equals("Pixel Density")) {
                phoneDetail.setPxDensity(finder.getDouble(val, 1).intValue());
            } else if (attr.equals("Display Subtype")) {
                phoneDetail.setDispType(val);
            } else if (attr.equals("Graphical Controller")) {
                phoneDetail.setgController(val);
            } else if (attr.equals("USB Connector")) {
                phoneDetail.setUsb(val);
            } else if (attr.equals("Bluetooth")) {
                phoneDetail.setBluetooth(val);
            } else if (attr.equals("NFC")) {
                if (val.equals("No")) {
                    phoneDetail.setNfc(false);
                } else {
                    phoneDetail.setNfc(true);
                }
            } else if (attr.equals("Number of effective pixels")) {
                phoneDetail.setNumOfEffPixels(val);
            } else if (attr.equals("Aperture (W)")) {
                phoneDetail.setAperture(val);
            } else if (attr.equals("Focus")) {
                phoneDetail.setFocus(val);
            } else if (attr.equals("Flash")) {
                phoneDetail.setFlash(val);
            } else if (attr.equals("Camera Extra Functions")) {
                phoneDetail.setCamExFunctions(val);
            } else if (attr.equals("Secondary Number of pixels")) {
                phoneDetail.setNumOfPixels2(val);
            } else if (attr.equals("Secondary Aperture (W)")) {
                phoneDetail.setAperture2(val);
            } else if (attr.equals("Secondary Camera Extra Functions")) {
                phoneDetail.setCamExFunctions2(val);
            } else if (attr.equals("Nominal Battery Capacity")) {
                phoneDetail.setBatteryCap(finder.getDouble(val, 1).intValue());
            } else if (attr.equals("Wireless Charging")) {
                phoneDetail.setWrlssCharging(val);
            } else if (attr.equals("Protection from solid materials")) {
                phoneDetail.setProtection("IP" + finder.getDouble(val, 1).intValue());
            } else if (attr.equals("Protection from liquids")) {
                phoneDetail.setProtection(phoneDetail.getProtection() + finder.getDouble(val, 1).intValue());
            } else if (attr.equals("Additional sensors")) {
                phoneDetail.setAddSensors(val);
            }
        }
        phoneDetailService.insertByPhoneDetail(phoneDetail);
    }
}
