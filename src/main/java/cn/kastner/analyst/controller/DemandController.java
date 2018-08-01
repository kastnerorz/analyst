package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.service.core.CategoryService;
import cn.kastner.analyst.service.core.DemandService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DemandController {

    private final NetResult netResult;

    private final DemandService demandService;

    private final CategoryService categoryService;

    public DemandController(NetResult netResult, DemandService demandService, CategoryService categoryService) {
        this.netResult = netResult;
        this.demandService = demandService;
        this.categoryService = categoryService;
    }

    /**
     * 获取指定需求
     * @param id 需求 id
     * @return 需求
     */
    @GetMapping(value = "/demands/{id}")
    public NetResult getDemand(@PathVariable("id") Long id) {
        Demand demand = demandService.findById(id);
        if (demand == null) {
            netResult.status = -1;
            netResult.message = "参数错误";
        } else {
            netResult.data = demand;
            netResult.status = 0;
        }
        return netResult;
    }

    @GetMapping(value = "/demands")
    public NetResult getDemands(Category category) {
        List<Demand> demands = demandService.findAllByCategory(category);
        if(!demands.isEmpty()){
            netResult.data = demands;
            netResult.status = 0;
            return netResult;
        }
        netResult.message = "没有需求";
        netResult.status = -1;
        return netResult;
    }

    /**
     * @param c {
     *          categoryId: '...' 品类ID
     * }
     * @param content 需求内容
     * @return 新建的需求
     */
    @RequestMapping(value = "/demands", method = RequestMethod.POST)
    public NetResult createDemands(Category c, @RequestParam String content) {
        Demand demand = demandService.findByContent(content);
        if (null != demand) {
            netResult.status = -1;
            netResult.message = "已经有这个需求";
        } else {
            demand = new Demand();
            Category category = categoryService.findById(c.getCategoryId());
            if (category == null) {
                netResult.status = -1;
                netResult.message = "品类ID错误";
            } else {
                demand.setCategory(category);
                demand.setContent(content);
                demand.setFlag((long)0);
                demandService.insertByDemand(demand);
                netResult.status = 0;
                netResult.data = demand;
            }
        }
        return netResult;
    }
}
