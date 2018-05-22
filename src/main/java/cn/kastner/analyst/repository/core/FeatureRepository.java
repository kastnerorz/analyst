package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.core.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeatureRepository extends JpaRepository<Feature, Long>,
        JpaSpecificationExecutor<Feature> {
    Feature findByFeatureId(Long featureId);
}
