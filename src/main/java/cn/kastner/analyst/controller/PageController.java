package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Category;
import cn.kastner.analyst.service.core.BrandService;
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
public class PageController {

    private final
    ItemService itemService;

    private final
    PriceService priceService;

    private final
    MainCrawlerService mainCrawlerService;

    private final
    CategoryService categoryService;

    private final
    BrandService brandService;

    @Autowired
    public PageController(ItemService itemService, PriceService priceService, CategoryService categoryService, MainCrawlerService mainCrawlerService, BrandService brandService) {
        this.itemService = itemService;
        this.priceService = priceService;
        this.mainCrawlerService = mainCrawlerService;
        this.categoryService = categoryService;
        this.brandService = brandService;

    }

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    /**
     * 搜索
     * @param keyword 关键词或链接
     * @param model 型号
     * @return 页面
     * @throws JSONException
     */
    @RequestMapping(value = "/search")
    public String search(@RequestParam String keyword, Model model) throws JSONException{
        model.addAttribute("keyword", keyword);
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);
        Category category = categoryService.findByLevelName(keyword);
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
            List<Price> prices = priceService.findByItemAndCrawDateTimeAfter(item, LocalDateTime.now().minusWeeks(1));
            if (!prices.isEmpty()) {
                price = prices.get(0).getPrice();
            }
            model.addAttribute("price", price);

            return "item";
        }
        else if(category!=null){
            Long categoryId = category.getCategoryId();
            String itemZhName;
            Long itemId;
            List<Item> items = itemService.findByCategoryId(categoryId);
            if (!items.isEmpty()) {
                Double price = (double) 0;
                for (int i = 0; i < items.size(); i++) {   //set i
                    Item item = items.get(i);
                    itemId=item.getItemId();
                    itemZhName = item.getZhName();   //get name
                    List<Price> prices = priceService.findByItemAndCrawDateTimeAfter(item, LocalDateTime.now().minusWeeks(1));
                    if (!prices.isEmpty()) {
                        price = prices.get(0).getPrice();  //get price
                    }
                    String imageList = item.getImageList();
                    String primaryImage = imageList.split(",")[0];  //get photo
                    System.out.print(itemId+itemZhName+price);
                    model.addAttribute("categoryId",categoryId);
                    model.addAttribute("itemId",itemId);
                    model.addAttribute("itemZhName", itemZhName);
                    model.addAttribute("price", price);
                    model.addAttribute("primaryImage", primaryImage);
                }
                return "category";
            }
            return null;
        }
        return "itemList";
    }

    /**
     * @param keyword
     * @param model
     * @return
     */
    @RequestMapping(value = "/report")
    public String report(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        Category category = categoryService.findByLevelName(keyword);
        if(category!=null){

            return "report";
        }
        return null;
    }

}

