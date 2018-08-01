package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.domain.PhoneParamGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PhoneParamGroupRepository extends JpaRepository<PhoneParamGroup, Long>,
        JpaSpecificationExecutor<PhoneParamGroupRepository> {
    List<PhoneParamGroup> findPhoneParamGroupByDemand(Demand demand);
    PhoneParamGroup findPhoneParamGroupById(Long id);
    PhoneParamGroup findPhoneParamGroupByCpuAndCpuClockAndDemandAndOsAndRamCapacityAndRamClockAndRamType(
        String cpu, Double cpuClock, Demand demand, String os, Double ramCapacity, Double ramClock, String ramType
    );
}
