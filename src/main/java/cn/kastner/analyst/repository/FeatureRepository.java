package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, String> {
}
