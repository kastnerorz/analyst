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

    /**
     * @param enName 品牌英文名
     * @return brand
     */
    Brand findByEnName(String enName);

    /**
     * @param zhName 中文名
     * @return 品牌
     */
    Brand findByZhName(String zhName);

    /**
     * @param enName 英文名
     * @param zhName 中文名
     * @return 品牌
     */
    Brand findByEnOrZhName(String enName, String zhName);


    /**
     * update a brand
     *
     * @param brand
     * @return brand
     */
    Brand update(Brand brand);

    /**
     * delete a brand
     *
     * @param brandId
     * @return brand
     */
    Brand delete(Long brandId);

}
