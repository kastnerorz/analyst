package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DemandRepository extends JpaRepository<Demand, Long>,
        JpaSpecificationExecutor<DemandRepository> {
    Demand findDemandById(Long demandId);
    List<Demand> findDemandsByCategory(Category category);
    Demand findDemandByContent(String content);
    List<Demand> findDemandsByCategoryAndContentContaining(Category category, String keyword);
}
