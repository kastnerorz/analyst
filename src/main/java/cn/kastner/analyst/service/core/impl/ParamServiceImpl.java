package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.core.Demand;
import cn.kastner.analyst.domain.core.Param;
import cn.kastner.analyst.repository.core.ParamRepository;
import cn.kastner.analyst.service.core.ParamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParamServiceImpl implements ParamService {

    private final ParamRepository paramRepository;

    public ParamServiceImpl(ParamRepository paramRepository) {
        this.paramRepository = paramRepository;
    }

    @Override
    public Param insertByParam(Param param) {
        return paramRepository.save(param);
    }

    @Override
    public Param findById(Long paramId) {
        return paramRepository.findParamById(paramId);
    }

    @Override
    public List<Param> findAll() {
        return paramRepository.findAll();
    }

    @Override
    public List<Param> findByDemandId(Long demandId) {
        return paramRepository.findParamsByDemandId(demandId);
    }

    @Override
    public Param update(Param param) {
        return paramRepository.save(param);
    }

    @Override
    public Param delete(Long paramId) {
        Param param = paramRepository.findParamById(paramId);
        paramRepository.delete(param);
        return param;
    }
}
