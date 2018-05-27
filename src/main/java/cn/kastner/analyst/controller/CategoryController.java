package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Category;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.util.NetResult;
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

    @RequestMapping(value="/getCategoryInfoByCategoryId")
    public NetResult getCategoryInfoByCategoryId(@RequestParam Long categoryId){
        Category category=categoryService.findById(categoryId);
        if(category !=null){
            netResult.data=category;
            netResult.status=0;
            return netResult;
        }
        netResult.message="No such category";
        netResult.status=-1;
        return netResult;
    }

    @RequestMapping(value="/getItemListByCategoryId")
    public NetResult getItemListByCategoryId(@RequestParam Long categoryId){
        List<Item> items=itemService.findByCategoryId(categoryId);
        if(!items.isEmpty()){
            netResult.data=items;
            netResult.status=0;
            return netResult;
        }
        netResult.message="No such items";
        netResult.status=-1;
        return netResult;
    }

    @RequestMapping(value = "/report")
    public String report(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        Category category = categoryService.findByLevelName(keyword);
        if(category!=null){

            return "report";
        }
//        Long categoryId = category.getCategoryId();
//        model.addAttribute("category", categoryId);
//        List<Brand> brands = brandService.findAll();
//        List brandSaleList;
//        Long brandId = (long) 0;
//        for (int i = 0; i < brands.size(); i++) {
//            brandId = brands.get(i).getBrandId();
//            List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
//            Long itemId = (long) 0;
//            Long brandSale=(long)0;
//            for (int j = 0; j < items.size(); j++) {
//                itemId = items.get(j).getItemId();
//                List<Price> prices=priceService.findByItemId(itemId);
//                Long sum=(long)0;
//                for(int m=0;m<prices.size();m++){
//                    sum=sum+prices.get(m).getVolume();
//                }
//                brandSale=brandSale+sum;
//            }
//            System.out.println(brands.get(i).getBrandZhName()+"的销售总量"+brandSale);
//        }
        return "categoryList";
    }

}
