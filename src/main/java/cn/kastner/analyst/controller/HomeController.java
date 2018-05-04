package cn.kastner.analyst.controller;

import cn.kastner.analyst.crawler.MainCrawler;
import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.Price;
import cn.kastner.analyst.repository.ItemRepository;
import cn.kastner.analyst.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.*;

@Controller
public class HomeController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PriceRepository priceRepository;

    @Autowired
    MainCrawler mainCrawler;

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);
//        System.out.println(keyword);
//        System.out.println(isMatch);

        if (isMatch) {
            Item item = mainCrawler.crawItemByUrl(keyword);
            model.addAttribute("itemId", item.getItemId());

            String itemCode = item.getModel();
            String cname = item.getZhName();
            String imageList = item.getImageList();
            String primaryImage = imageList.split("\\\",\\\"")[0];
            model.addAttribute("primaryImage", primaryImage);
            model.addAttribute("cname", cname);
            model.addAttribute("itemCode", itemCode);

            Double price = (double) 0;
            List<Price> prices = priceRepository.findPriceByItem(item);
            if (prices.size() != 0) {
                price = prices.get(0).getPrice();
            }
            model.addAttribute("price", price);

            return "item";
        }
        return "itemList";
    }

}

