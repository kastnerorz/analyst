package cn.kastner.analyst.controller;

import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.domain.Param;
import cn.kastner.analyst.service.core.ParamService;
import cn.kastner.analyst.util.NetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParamController {

    private final ParamService paramService;
    private final NetResult netResult;

    @Autowired
    public ParamController(ParamService paramService, NetResult netResult) {
        this.paramService = paramService;
        this.netResult = netResult;
    }

    @GetMapping(value = "/params/{id}")
    public NetResult getParam(@PathVariable("id") Long id) {
        Param param = paramService.findById(id);
        if (null == param) {
            netResult.status = -1;
            netResult.message = "参数错误";
        } else {
            netResult.status = 0;
            netResult.data = param;
        }
        return netResult;
    }

//    @GetMapping(value = "/params")
//    private NetResult getParams(Demand demand) {
//        List<Param> param
//    }
}
