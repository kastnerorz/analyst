package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.core.Demand;
import cn.kastner.analyst.domain.core.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ParamRepository extends JpaRepository<Param, Long>,
        JpaSpecificationExecutor<ParamRepository> {
    Param findParamById(Long paramId);
    List<Param> findParamsByDemandId(Long demandId);
}
