package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.core.Category;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
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

    private final
    CategoryService categoryService;

    @Autowired
    public HomeController(ItemService itemService, PriceService priceService, CategoryService categoryService, MainCrawlerService mainCrawlerService) {
        this.itemService = itemService;
        this.priceService = priceService;
        this.mainCrawlerService = mainCrawlerService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/report")
    public String report() {
        return "report";
    }


    @RequestMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) throws JSONException {
        model.addAttribute("keyword", keyword);
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);

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
            if (!prices.isEmpty()) {
                price = prices.get(0).getPrice();
            }
            model.addAttribute("price", price);

            return "item";
        }
        return "itemList";
    }

    @RequestMapping(value = "/category")
    public String category(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        Category category = categoryService.findByLevelName(keyword);
        model.addAttribute("category", category.getCategoryId());
        System.out.print(keyword);
        Long categoryId = category.getCategoryId();
        System.out.print(categoryId);
        String itemZhName;
        Double price = (double) 0;
        List<Item> items = itemService.findByCategoryId(categoryId);
        if (!items.isEmpty()) {
            for (int i = 0; i < 5; i++) {   //set i
                Item item=items.get(i);
                itemZhName = item.getZhName();   //get name
                List<Price> prices=priceService.findByItemAndCrawDateTime(item,LocalDateTime.now().minusWeeks(1));
                if(!prices.isEmpty()){
                    price=prices.get(0).getPrice();  //get price
                }
                String imageList = item.getImageList();
                String primaryImage = imageList.split(",")[0];  //get photo
//                System.out.print(itemZhName);
//                System.out.print(price);
                model.addAttribute("itemZhName", itemZhName);
                model.addAttribute("price",price);
                model.addAttribute("primaryImage", primaryImage);
            }

        }

        return "category";
    }
}

