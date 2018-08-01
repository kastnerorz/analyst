package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.domain.PhoneParamGroup;
import cn.kastner.analyst.repository.core.PhoneParamGroupRepository;
import cn.kastner.analyst.service.core.DemandService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PhoneParamGroupController {

    private final PhoneParamGroupRepository phoneParamGroupRepository;

    private final DemandService demandService;

    private final NetResult netResult;

    @Autowired
    public PhoneParamGroupController(PhoneParamGroupRepository phoneParamGroupRepository, DemandService demandService, NetResult netResult) {
        this.phoneParamGroupRepository = phoneParamGroupRepository;
        this.demandService = demandService;
        this.netResult = netResult;
    }

    @GetMapping(value = "/phoneParamGroup/{id}")
    public NetResult getPhoneParamGroup(@PathVariable("id") Long id) {
        PhoneParamGroup phoneParamGroup = phoneParamGroupRepository.findPhoneParamGroupById(id);
        if (phoneParamGroup == null) {
            netResult.status = -1;
            netResult.message = "没有这个参数组合";
        } else {
            netResult.status = 0;
            netResult.message = "查找成功";
            netResult.data = phoneParamGroup;
        }
        return netResult;
    }

    @GetMapping(value = "/phoneParamGroup")
    public NetResult getPhoneParamGroups(Demand demand) {
        List<PhoneParamGroup> phoneParamGroups = phoneParamGroupRepository.findPhoneParamGroupByDemand(demand);
        if (phoneParamGroups.isEmpty()) {
            netResult.status = -1;
            netResult.message = "参数错误";
        } else {
            netResult.status = 0;
            netResult.message = "查找成功";
            netResult.data = phoneParamGroups;
        }
        return netResult;
    }
}
