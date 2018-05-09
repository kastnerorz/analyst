package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Brand;

import java.util.List;

public interface BrandService {

    /**
     * @param brand
     * @return brand
     */
    Brand insertByBrand(Brand brand);

    
    List<Brand> findAll();


}
