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
            tmp.put("itemNum", items.size());
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

    @RequestMapping(value = "/getItemNumByPriceDis")
    public NetResult getItemNumByPriceDis(@RequestParam Long categoryId) {    //一个类别下按价格区间统计所有的item
        List<Brand> brands = brandService.findAll();
        Long brandId;
        Double min = (double) 0;
        Double max = (double) 1000;
        List<HashMap<String, Object>> data = new ArrayList<>();
        while (min > 5999) {    //根据每个区间遍历所有brand下的item总数
            Long sum = (long) 0;
            for (Brand brand : brands) {    //遍历每个brand得到该区间下的item
                brandId = brand.getBrandId();
                List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
                if (!items.isEmpty()) {
                    List<Price> prices = priceService.findPriceListByPriceAndItem(min, max, items);
                    sum = sum + prices.size();
                }
            }
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("price", min);
            tmp.put("sum", sum);
            data.add(tmp);
            min = min + 1000;
            max = max + 1000;
        }
        Long sum = (long) 0;
        for (Brand brand : brands) {
            brandId = brand.getBrandId();
            List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
            if (!items.isEmpty()) {
                List<Price> prices = priceService.findPriceListByPriceMin(min, items);
                sum = sum + prices.size();
            }
        }
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put("price", min);
        tmp.put("sum", sum);
        data.add(tmp);

        netResult.data = data;
        netResult.status = 0;
        return netResult;

    }
}
