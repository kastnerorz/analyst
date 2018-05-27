package cn.kastner.analyst.controller;


import cn.kastner.analyst.domain.core.Brand;
import cn.kastner.analyst.domain.core.Category;
import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.core.Price;
import cn.kastner.analyst.service.core.BrandService;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.ItemService;
import cn.kastner.analyst.service.core.PriceService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ReportController {
    private final ItemService itemService;
    private final PriceService priceService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final NetResult netResult;

    @Autowired
    public ReportController(ItemService itemService, PriceService priceService, BrandService brandService, CategoryService categoryService, NetResult netResult) {
        this.itemService = itemService;
        this.priceService = priceService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.netResult = netResult;
    }

    @RequestMapping(value = "/getTopTenSaleAndItemNumByCategoryId")
    public NetResult getTopTenSaleAndItemNumByCategoryId(@RequestParam Long categoryId) {
        List<Brand> brands = brandService.findAll();   //All brand
        Long brandId = (long) 0;
        String brandName;
        List<HashMap<String, Object>> data = new ArrayList<>();
        for (Brand brand : brands) {
            brandId = brand.getBrandId();
            brandName = brand.getBrandZhName();
            List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
            Long brandSale = (long) 0;
            for (Item item : items) {
                List<Price> prices = priceService.findByItem(item);
                Long sum = (long) 0;
                for (Price price : prices) {
                    sum = sum + price.getVolume();
                }
                brandSale = brandSale + sum;
            }
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("brandName", brandName);
            tmp.put("brandSale", brandSale);
            tmp.put("brandId", brandId);
            tmp.put("itemNum",items.size());
            data.add(tmp);
//            System.out.println(brand.getBrandZhName() + "的销售总量" + brandSale);
        }
        data.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (int) ((long) o2.get("brandSale") - (long) o1.get("brandSale"));
            }
        });
        netResult.data = data;
        netResult.status = 0;
        return netResult;
    }


}
