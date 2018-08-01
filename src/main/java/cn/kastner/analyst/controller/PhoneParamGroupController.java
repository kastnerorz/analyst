package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.domain.PhoneParamGroup;
import cn.kastner.analyst.repository.core.PhoneParamGroupRepository;
import cn.kastner.analyst.service.core.DemandService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(value = "/phoneParamGroups/{id}")
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

    @GetMapping(value = "/phoneParamGroups")
    public NetResult getPhoneParamGroups(Demand d) {
        Demand demand = demandService.findById(d.getId());
        if (demand == null) {
            netResult.status = -1;
            netResult.message = "提供的 demandId 有误";
        } else {
            List<PhoneParamGroup> phoneParamGroups = phoneParamGroupRepository.findPhoneParamGroupByDemand(demand);
            if (phoneParamGroups.isEmpty()) {
                netResult.status = -1;
                netResult.message = "参数错误";
            } else {
                netResult.status = 0;
                netResult.message = "查找成功";
                netResult.data = phoneParamGroups;
            }
        }
        return netResult;
    }

    @PostMapping(value = "/phoneParamGroups")
    public NetResult createPhoneParamGroup(PhoneParamGroup p, @RequestParam Demand d) {
        Demand demand = demandService.findById(d.getId());
        if (demand == null) {
            netResult.status = -1;
            netResult.message = "提供的 demandId 有误";
        } else {
            PhoneParamGroup phoneParamGroup = new PhoneParamGroup();
            phoneParamGroup.setCpu(p.getCpu());
            phoneParamGroup.setCpuClock(p.getCpuClock());
            phoneParamGroup.setDemand(demand);
            phoneParamGroup.setOs(p.getOs());
            phoneParamGroup.setRamCapacity(p.getRamCapacity());
            phoneParamGroup.setRamClock(p.getRamClock());
            phoneParamGroup.setRamType(p.getRamType());
            phoneParamGroupRepository.save(phoneParamGroup);
            netResult.status = 0;
            netResult.message = "创建成功";
            netResult.data = phoneParamGroup;
        }
        return netResult;
    }
}
