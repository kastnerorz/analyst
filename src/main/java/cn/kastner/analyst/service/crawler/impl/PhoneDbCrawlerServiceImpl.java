package cn.kastner.analyst.service.crawler.impl;

import cn.kastner.analyst.domain.Brand;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.PhoneDetail;
import cn.kastner.analyst.service.core.BrandService;
import cn.kastner.analyst.service.crawler.PhoneDbCrawlerService;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import cn.kastner.analyst.util.crawler.Finder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhoneDB 爬虫服务实现
 */
@Service
public class PhoneDbCrawlerServiceImpl implements PhoneDbCrawlerService {

    private static String strClassName = PhoneDbCrawlerServiceImpl.class.getName();
    private static Logger logger = Logger.getLogger(strClassName);

    private Item item;
    private final BrandService brandService;
    private final Finder finder;
    private final PhoneDetailService phoneDetailService;

    @Autowired
    public PhoneDbCrawlerServiceImpl(BrandService brandService, Finder finder, PhoneDetailService phoneDetailService) {
        this.brandService = brandService;
        this.finder = finder;
        this.phoneDetailService = phoneDetailService;
    }


    /**
     * PhoneDB 爬虫主函数
     * @param item 商品对象
     */
    @Override
    public void crawl(Item item) throws IOException {
        this.item = item;
        PhoneDetail phoneDetailDb = phoneDetailService.findById(item.getId());
        if (phoneDetailDb == null) {
            String url = searchByModelAndRom(item.getModel(), phoneDetailService.findById(item.getId()).getRomCapacity().toString());
            if (url != null) {
                crawDetails(url);
            }
        }
    }

    /**
     * @param model 型号
     * @param rom   存储空间
     * @return PhoneDB详情链接
     */
    @Override
    public String searchByModelAndRom(String model, String rom) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        headers.put("Cache-Control", "max-age=0");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Upgrade-Insecure-Requests", "1");
        HashMap<String, String> data = new HashMap<>();
        data.put("model", model);
        data.put("rom_cap_b", rom + " GB");
        Document doc = Jsoup.connect("http://phonedb.net/index.php?m=device&s=query")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/66.0.3359.181 Safari/537.36")
                    .headers(headers)
                    .data(data)
                    .post();
        try {
            Element resultEl = doc.getElementsByAttributeValue("title", "See detailed datasheet")
                    .get(0);
            return resultEl.attr("href");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param url PhoneDB详情链接
     */
    @Override
    public void crawDetails(String url) throws IOException {
        HashMap<String , String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.9");
        headers.put("Cache-Control", "no-cache");
        headers.put("Connection", "keep-alive");
        headers.put("Host", "phonedb.net");
        headers.put("Pragma", "no-cache");
        headers.put("Referer", "http://phonedb.net/index.php?m=device&s=query");
        headers.put("Upgrade-Insecure-Requests", "1");

        System.getProperties().setProperty("proxySet", "true");
        //用的代理服务器
        System.getProperties().setProperty("http.proxyHost", "127.0.0.1");
        //代理端口
        System.getProperties().setProperty("http.proxyPort", "2333");

        Document doc = Jsoup.connect("http://phonedb.net/" + url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4)" +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            "Chrome/66.0.3359.181 Safari/537.36")
                    .headers(headers)
                    .post();
        Element dataTableEl = doc.select("table").first();
        Elements dataTrEls = dataTableEl.select("tr");
        PhoneDetail phoneDetail = phoneDetailService.findById(item.getId());
        phoneDetail.setCrawDate(LocalDate.now());
        int index = 0;
        for(Element el: dataTrEls) {
            index++;
            Elements dataTdEls = el.select("td");
            if (dataTdEls.size() == 1) {
                continue;
            }
            String attr = dataTdEls.first().text();
            String val = dataTdEls.get(1).text();

            switch (attr) {
                case "Brand":
                    logger.info("attr   ->" + attr);
                    Brand brandDb = brandService.findByEnName(attr.toUpperCase());
                    if (brandDb == null) {
                        Brand brand = new Brand();
                        brand.setBrandEnName(val.toUpperCase());
                        phoneDetail.setBrand(brand);
                        brandService.insertByBrand(brand);
                    } else {
                        phoneDetail.setBrand(brandDb);
                    }
                    break;
                case "Model":
                    phoneDetail.setModel(val);
                    break;
                case "Released":
                    // TODO handle date (year, month)
                    break;
                case "Additional Features":
                    phoneDetail.setAddlFeatures(val);
                    break;
                case "Mobile Operator":
                    phoneDetail.setOperator(val);
                    break;
                case "Width":
                    phoneDetail.setWidth(finder.getDouble(val, 1));
                    break;
                case "Height":
                    phoneDetail.setHeight(finder.getDouble(val, 1));
                    break;
                case "Depth":
                    phoneDetail.setDepth(finder.getDouble(val, 1));
                    break;
                case "Mass":
                    phoneDetail.setMass(finder.getDouble(val, 1));
                    break;
                case "Platform":
                    phoneDetail.setPlatform(val);
                    break;
                case "Operating System":
                    phoneDetail.setOs(val);
                    break;
                case "Software Extras":
                    phoneDetail.setSwExtras(val);
                    break;
                case "CPU Clock":
                    phoneDetail.setCpuClock(finder.getDouble(val, 1));
                    break;
                case "RAM Type":
                    phoneDetail.setRamType(val);
                    Element ramClkTrEl = dataTrEls.get(index + 1);
                    Elements ramClkTdEls = ramClkTrEl.getElementsByTag("td");
                    phoneDetail.setRamCapacity(finder.getDouble(ramClkTdEls.get(1).text(), 1));
                    break;
                case "RAM Capacity":
                    phoneDetail.setRamCapacity(finder.getDouble(val, 1));
                    break;
                case "Non-volatile Memory Type":
                    phoneDetail.setRomType(val);
                    break;
                case "Non-volatile Memory Capacity":
                    phoneDetail.setRomCapacity(finder.getDouble(val, 1));
                    break;
                case "Display Resolution":
                    Pattern pattern = Pattern.compile("(.*)x(.*)");
                    Matcher matcher = pattern.matcher(val);
                    matcher.find();
                    phoneDetail.setWideRez(Integer.parseInt(matcher.group(1)));
                    phoneDetail.setLongRez(Integer.parseInt(matcher.group(2)));
                    break;
                case "Display Diagonal":
                    Element dispDiagonalTrEl = dataTrEls.get(index + 1);
                    Elements dispDiagonalTdEls = dispDiagonalTrEl.getElementsByTag("td");
                    phoneDetail.setDispDiagonal(finder.getDouble(dispDiagonalTdEls.get(1).text(), 1));
                    break;
                case "Display Area Utilization":
                    phoneDetail.setDispAreaUtilization(finder.getDouble(val, 1).intValue());
                    break;
                case "Pixel Density":
                    phoneDetail.setPxDensity(finder.getDouble(val, 1).intValue());
                    break;
                case "Display Subtype":
                    phoneDetail.setDispType(val);
                    break;
                case "Graphical Controller":
                    phoneDetail.setgController(val);
                    break;
                case "USB Connector":
                    phoneDetail.setUsb(val);
                    break;
                case "Bluetooth":
                    phoneDetail.setBluetooth(val);
                    break;
                case "NFC":
                    if (val.equals("No")) {
                        phoneDetail.setNfc(false);
                    } else {
                        phoneDetail.setNfc(true);
                    }
                    break;
                case "Number of effective pixels":
                    phoneDetail.setNumOfEffPixels(val);
                    break;
                case "Aperture (W)":
                    phoneDetail.setAperture(val);
                    break;
                case "Focus":
                    phoneDetail.setFocus(val);
                    break;
                case "Flash":
                    phoneDetail.setFlash(val);
                    break;
                case "Camera Extra Functions":
                    phoneDetail.setCamExFunctions(val);
                    break;
                case "Secondary Number of pixels":
                    phoneDetail.setNumOfPixels2(val);
                    break;
                case "Secondary Aperture (W)":
                    phoneDetail.setAperture2(val);
                    break;
                case "Secondary Camera Extra Functions":
                    phoneDetail.setCamExFunctions2(val);
                    break;
                case "Nominal Battery Capacity":
                    phoneDetail.setBatteryCap(finder.getDouble(val, 1).intValue());
                    break;
                case "Wireless Charging":
                    phoneDetail.setWrlssCharging(val);
                    break;
                case "Protection from solid materials":
                    phoneDetail.setProtection("IP" + finder.getDouble(val, 1).intValue());
                    break;
                case "Protection from liquids":
                    phoneDetail.setProtection(phoneDetail.getProtection() + finder.getDouble(val, 1).intValue());
                    break;
                case "Additional sensors":
                    phoneDetail.setAddSensors(val);
                    break;
            }
        }
        phoneDetailService.insertByPhoneDetail(phoneDetail);
    }
}
