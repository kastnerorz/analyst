package cn.kastner.analyst.controller;


import cn.kastner.analyst.domain.*;
import cn.kastner.analyst.service.core.*;
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
    private final DemandService demandService;
    private final ParamService paramService;

    @Autowired
    public ReportController(ItemService itemService, PriceService priceService, BrandService brandService,
                            CategoryService categoryService, NetResult netResult,
                            DemandService demandService, ParamService paramService) {
        this.itemService = itemService;
        this.priceService = priceService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.netResult = netResult;
        this.demandService = demandService;
        this.paramService = paramService;
    }


    /**
     * 市场占比和品牌的商品总数和品牌的平均价格
     * @param categoryId
     * @return
     */
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
            Double total = (double) 0;
            for (Item item : items) {
                List<Price> prices = priceService.findByItem(item);
                Long sum = (long) 0;
                for (Price price : prices) {
//                    sum = sum + price.getVolume();
                    total = total + price.getPrice();
                }
                brandSale = brandSale + sum;
            }

            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("brandName", brandName);
            tmp.put("brandSale", brandSale);
            tmp.put("brandId", brandId);
            tmp.put("itemNum", items.size());
            tmp.put("average",total/items.size());
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

    /**
     * 一个类别下按价格区间统计所有的item
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "/getItemNumByPriceDis")
    public NetResult getItemNumByPriceDis(@RequestParam Long categoryId) {    //
        List<Brand> brands = brandService.findAll();
//        System.out.println(brands.get(0).getBrandZhName());
        Long brandId;
        Double min;
        Double max;
        List<HashMap<String, Object>> priceSum = new ArrayList<>();
        if (categoryId == 4) {
            min = (double) 0;
            max = (double) 1000;
            while (min < 5999) {    //根据每个j区间遍历所有brand下的item总数
                Long sum = (long) 0;   //每个区间的商品总数
                Long saleTotal=(long)0; //每个区间的价格总销量
                for (Brand brand : brands) {    //遍历每个brand得到该区间下的item
                    brandId = brand.getBrandId();
                    List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
//                System.out.println(items.get(0).getZhName());
                    List<Long> itemIdList = new ArrayList<>();   //存放itemId
                    for (Item item : items) {
                        Long itemId = item.getId();
                        itemIdList.add(itemId);
                    }
//                System.out.println(itemIdList);
                    if (!itemIdList.isEmpty()) {   //找到每个brand下这个价格区间的商品数
                        List<Price> prices = priceService.findPriceListByPriceAndItem(min, max, itemIdList);
                        sum = sum + prices.size();
                        for(Price price:prices){
//                            saleTotal=saleTotal+price.getVolume();
                        }

//                    System.out.println(sum);
                    }
                }
                HashMap<String, Object> tmp = new HashMap<>();
                tmp.put("price", min);
                tmp.put("sum", sum);
                tmp.put("saleTotal",saleTotal);
                priceSum.add(tmp);
                min = min + 1000;
                max = max + 1000;
            }
            //对7000+以上的价格遍历每个brand（感觉这样子复杂度太高了，哭了）
            Long sum = (long) 0;
            Long saleTotal=(long) 0;
            for (Brand brand : brands) {
                brandId = brand.getBrandId();
                List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
                List<Long> itemIdList = new ArrayList<>();
                for (Item item : items) {
                    Long itemId = item.getId();
                    itemIdList.add(itemId);
                }
                if (!itemIdList.isEmpty()) {
                    List<Price> prices = priceService.findPriceListByPriceMin(min, itemIdList);
                    sum = sum + prices.size();
                    for(Price price:prices){
//                        saleTotal=saleTotal+price.getVolume();
                    }
                }
            }
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("price", min);
            tmp.put("sum", sum);
            tmp.put("saleTotal",saleTotal);
            priceSum.add(tmp);
        } else {
            //电脑3000,6000,9000,11000,14000;
            min = (double) 1000;
            max = (double) 3000;
            while (min < 13999) {    //根据每个j区间遍历所有brand下的item总数
                Long sum = (long) 0;
                Long saleTotal=(long)0;
                for (Brand brand : brands) {    //遍历每个brand得到该区间下的item
                    brandId = brand.getBrandId();
                    List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
                    List<Long> itemIdList = new ArrayList<>();   //存放itemId
                    for (Item item : items) {
                        Long itemId = item.getId();
                        itemIdList.add(itemId);
                    }
                    if (!itemIdList.isEmpty()) {   //找到每个brand下这个价格区间的商品数
                        List<Price> prices = priceService.findPriceListByPriceAndItem(min, max, itemIdList);
                        sum = sum + prices.size();
                        for(Price price:prices){
//                            saleTotal=saleTotal+price.getVolume();
                        }
                    }
                }
                HashMap<String, Object> tmp = new HashMap<>();
                tmp.put("price", min);
                tmp.put("sum", sum);
                tmp.put("saleTotal",saleTotal);
                priceSum.add(tmp);
                min = min + 3000;
                max = max + 3000;
            }
            //对14000+以上的价格遍历每个brand（感觉这样子复杂度太高了，哭了）
            Long sum = (long) 0;
            Long saleTotal=(long)0;
            for (Brand brand : brands) {
                brandId = brand.getBrandId();
                List<Item> items = itemService.findByBrandIdAndCategoryId(brandId, categoryId);
                List<Long> itemIdList = new ArrayList<>();
                for (Item item : items) {
                    Long itemId = item.getId();
                    itemIdList.add(itemId);
                }
                if (!itemIdList.isEmpty()) {
                    List<Price> prices = priceService.findPriceListByPriceMin(min, itemIdList);
                    sum = sum + prices.size();
                    for(Price price:prices){
//                        saleTotal=saleTotal+price.getVolume();
                    }
                }
            }
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("price", min);
            tmp.put("sum", sum);
            tmp.put("saleTotal",saleTotal);
            priceSum.add(tmp);
        }

        netResult.data = priceSum;
        netResult.status = 0;
        return netResult;
    }

    /**
     * 根据demand和param里的flag判断
     * @param category
     * @return
     */
    @RequestMapping(value = "/getFocusByCategoryId")
    public NetResult getFocusByCategoryId(Category category) {
        List<Demand> demands = demandService.findAllByCategory(category);
        //[{name:   flag:  },{}]
        List<HashMap<String, Object>> focus = new ArrayList<>();
        for (Demand demand : demands) {
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("name", demand.getContent());
            tmp.put("flag", demand.getFlag());
            focus.add(tmp);
            List<Param> params = paramService.findByDemand(demand);
            for (Param param : params) {
                HashMap<String, Object> tmp2 = new HashMap<>();
                tmp2.put("name", param.getValue());
                tmp2.put("flag", param.getFlag());
                focus.add(tmp2);
            }
        }
        //从大到小排序
        focus.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                return (int) ((long) o2.get("flag") - (long) o1.get("flag"));
            }
        });
        netResult.data = focus;
        netResult.status = 0;
        return netResult;
    }


}
