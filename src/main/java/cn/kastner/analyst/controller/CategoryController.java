package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Category;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.util.NetResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final ItemService itemService;

    private final PriceService priceService;

    private final NetResult netResult;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService,PriceService priceService,NetResult netResult){
        this.priceService=priceService;
        this.categoryService=categoryService;
        this.itemService=itemService;
        this.netResult=netResult;

    }

    @JsonIgnore
    @RequestMapping(value="/getCategoryInfoByCategoryId")
    public NetResult getCategoryInfoByCategoryId(@RequestParam Long categoryId) {
        Category category = categoryService.findById(categoryId);
        System.out.print(category.getCategoryId());
        if (category != null) {
            netResult.data = category;
            netResult.status = 0;
            return netResult;
        } else {
            netResult.message = "No such category";
            netResult.status = -1;
            return netResult;
        }
    }

    @RequestMapping(value="/getItemListByCategoryId")
    public NetResult getItemListByCategoryId(@RequestParam Long categoryId){
        List<Item> items=itemService.findByCategoryId(categoryId);
        if(!items.isEmpty()) {
            netResult.data = items;
            netResult.status = 0;
            return netResult;
        }else{
            netResult.message="No such items";
            netResult.status = -1;
            return netResult;
        }
    }


}
