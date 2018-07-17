package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandRepository extends JpaRepository<Brand,Long>,
        JpaSpecificationExecutor<Brand> {
    Brand findByBrandEnName(String brandEnName);
    Brand findByBrandZhName(String brandZhName);
    Brand findByBrandEnNameOrBrandZhName(String enName, String zhName);
    Brand findByBrandId(Long brandId);
}
