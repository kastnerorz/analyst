package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.core.*;
import cn.kastner.analyst.service.core.*;
import cn.kastner.analyst.util.NetResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    private final ItemService itemService;


    private final NetResult netResult;

    private final DemandService demandService;

    private final ParamService paramService;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService, NetResult netResult,
                              DemandService demandService,ParamService paramService){
        this.categoryService=categoryService;
        this.itemService=itemService;
        this.netResult=netResult;
        this.demandService=demandService;
        this.paramService=paramService;

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

    @RequestMapping(value="getNeedListByCategoryId")
    public NetResult getNeedListCategory(@RequestParam Category category){
        //一开始展示类别下的所有需求
        List<Demand> demands=demandService.findAllByCategory(category);
        List<HashMap<String,Object>> data=new ArrayList<>();
        if(!demands.isEmpty()){
            for(Demand demand:demands){
                    List<Param> params=paramService.findByDemand(demand);
                    HashMap<String,Object> tmp=new HashMap<>();
                    for(Param param:params){
                        tmp.put("name",param.getName());
                        tmp.put("value",param.getValue());
                        data.add(tmp);
                    }
            }
            netResult.data=data;
            netResult.status=0;
            return netResult;
        }
        netResult.message="No such demands";
        netResult.status=-1;
        return netResult;
    }


}
