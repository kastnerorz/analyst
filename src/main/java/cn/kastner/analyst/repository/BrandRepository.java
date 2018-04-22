package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,String> {
    Brand findByBrandEnName(String brandEnName);
}
