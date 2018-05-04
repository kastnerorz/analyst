package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeatureRepository extends JpaRepository<Feature, Long>,
        JpaSpecificationExecutor<Feature> {
}
