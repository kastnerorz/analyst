package cn.kastner.analyst.controller;

import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import cn.kastner.analyst.repository.core.ItemRepository;
import cn.kastner.analyst.repository.core.PriceRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.*;

@Controller
public class HomeController {

    private final
    ItemService itemService;

    private final
    PriceService priceService;

    private final
    MainCrawlerService mainCrawlerService;

    @Autowired
    public HomeController(ItemService itemService, PriceService priceService, MainCrawlerService mainCrawlerService) {
        this.itemService = itemService;
        this.priceService = priceService;
        this.mainCrawlerService = mainCrawlerService;
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) throws JSONException {
        model.addAttribute("keyword", keyword);
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);
//        System.out.println(keyword);
//        System.out.println(isMatch);

        if (isMatch) {
            Item item = mainCrawlerService.crawItemByUrl(keyword);
            model.addAttribute("itemId", item.getItemId());

            String itemCode = item.getModel();
            String cname = item.getZhName();
            String imageList = item.getImageList();
            String primaryImage = imageList.split(",")[0];
            model.addAttribute("primaryImage", primaryImage);
            model.addAttribute("cname", cname);
            model.addAttribute("itemCode", itemCode);

            Double price = (double) 0;
            List<Price> prices = priceService.findByItemAndCrawDateTime(item, LocalDateTime.now().minusWeeks(1));
            if (prices.size() != 0) {
                price = prices.get(0).getPrice();
            }
            model.addAttribute("price", price);

            return "item";
        }
        return "itemList";
    }

}

