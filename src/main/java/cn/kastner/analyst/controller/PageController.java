package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.service.core.BrandService;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.service.crawler.MainCrawlerService;
import cn.kastner.analyst.domain.Item;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public PageController(ItemService itemService,
                          PriceService priceService,
                          CategoryService categoryService,
                          MainCrawlerService mainCrawlerService,
                          BrandService brandService) {
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

        // 如果搜索关键词为链接
        Boolean isMatch = Pattern.matches(".*https?.*", keyword);
        if (isMatch) {
            Item item = mainCrawlerService.crawl(keyword);
            String primaryImage = item.getImageGroup().split(",")[0];
            model.addAttribute("primaryImage", primaryImage);
            model.addAttribute("zhName", item.getZhName());
            model.addAttribute("itemModel", item.getModel());
            model.addAttribute("id", item.getId());
            model.addAttribute("price", priceService.findLatestByItem(item).getPrice());
            return "item";
        }

        // 如果搜索关键词为品类
        Category category = categoryService.findByLevelName(keyword);
        if(category!=null) {
            model.addAttribute("categoryId",category.getCategoryId());
            return "category";
        }

        // 直接搜索关键词
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

