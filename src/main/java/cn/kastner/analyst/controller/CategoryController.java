package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.*;
import cn.kastner.analyst.service.core.*;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import cn.kastner.analyst.util.NetResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final ItemService itemService;


    private final NetResult netResult;

    private final DemandService demandService;

    private final ParamService paramService;

    private final PhoneDetailService phoneDetailService;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService, NetResult netResult,
                              DemandService demandService, ParamService paramService, PhoneDetailService phoneDetailService){
        this.categoryService=categoryService;
        this.itemService=itemService;
        this.netResult=netResult;
        this.demandService=demandService;
        this.paramService=paramService;

        this.phoneDetailService = phoneDetailService;
    }


    /**
     * 获取品类信息
     * @param id  品类 id
     * @return {
     *     data: {
     *         category: {
     *             ...
     *         }
     *     }
     * }
     */
    @GetMapping(value= "/category")
    public NetResult getCategoryById(@RequestParam Long id) {
        Category category = categoryService.findById(id);
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

    /**
     * 获取品类下的商品列表
     * @param category 品类
     * @return {
     *
     * }
     */
    @GetMapping(value= "/items")
    public NetResult getItemListByCategory(Category category){
        List<Item> items=itemService.findByCategory(category);
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




    @GetMapping(value = "/params")
    public NetResult getParamsByDemand(Demand demand) {
        List<Param> paramList = paramService.findByDemand(demand);
        if (!paramList.isEmpty()) {
             netResult.data = paramList;
             netResult.status = 0;
        } else {
            netResult.message = "没有这个需求！";
            netResult.status = -1;
        }
        return netResult;
    }

    @RequestMapping(value = "/itemsAfterFilter")
    public NetResult getItemsAfterFilter(@RequestParam List<Integer> batteryCap,
                                         @RequestParam List<Double> cpuClock,
                                         @RequestParam List<Double> romCapacity,
                                         @RequestParam List<Double> ramCapacity,
                                         @RequestParam List<Integer> pxDensity) {
        List<PhoneDetail> phoneDetailList = phoneDetailService.findByItemFilter(batteryCap, cpuClock, romCapacity, ramCapacity, pxDensity);
        List<Item> itemList = new ArrayList<>();
        if (!phoneDetailList.isEmpty()) {
            for (PhoneDetail e: phoneDetailList) {
                Item item = itemService.findById(e.getId());
                itemList.add(item);
            }
        }
        netResult.data = itemList;
        netResult.status = 0;
        return netResult;
    }

    @RequestMapping(value="/getItemListAfterDemand")
    //根据需求刷新页面
    public NetResult getItemListAfterDemand(@RequestParam List<String> demands ,Long category){









        return netResult;
    }

}
