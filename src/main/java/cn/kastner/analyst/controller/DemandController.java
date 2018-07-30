package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.service.core.DemandService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemandController {

    private final NetResult netResult;

    private final DemandService demandService;

    public DemandController(NetResult netResult, DemandService demandService) {
        this.netResult = netResult;
        this.demandService = demandService;
    }

    /**
     * 获取指定需求
     * @param id 需求 id
     * @return {
     *
     * }
     */
    @GetMapping(value = "demand/{id}")
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

    @GetMapping(value = "demand")
    public NetResult getDemands(Category category){
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
}
