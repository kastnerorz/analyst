package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Brand;

import java.util.List;

public interface BrandService {

    /**
     * create a brand
     *
     * @param brand
     * @return brand
     */
    Brand insertByBrand(Brand brand);

    /**
     * delete a brand
     *
     * @param brandId
     * @return brand
     */
    Brand delete(Long brandId);

    /**
     * update a brand
     *
     * @param brandId
     * @return brand
     */
    Brand update(Long brandId);

    /**
     * retrieve a brand
     *
     * @param brandId
     * @return brand
     */
    Brand findById(Long brandId);

    /**
     * retrieve all brands
     *
     * @return brand list
     */
    List<Brand> findAll();


}
