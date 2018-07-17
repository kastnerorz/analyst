package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Brand;
import cn.kastner.analyst.repository.core.BrandRepository;
import cn.kastner.analyst.service.core.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private final
    BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand insertByBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand findById(Long brandId) {
        return brandRepository.findByBrandId(brandId);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findByEnName(String enName) {
        return brandRepository.findByBrandEnName(enName);
    }

    @Override
    public Brand findByZhName(String zhName) {
        return brandRepository.findByBrandZhName(zhName);
    }

    @Override
    public Brand findByEnOrZhName(String enName, String zhName) {
        return brandRepository.findByBrandEnNameOrBrandZhName(enName, zhName);
    }

    @Override
    public Brand update(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand delete(Long brandId) {
        Brand brand = brandRepository.findByBrandId(brandId);
        brandRepository.delete(brand);
        return brand;
    }




}
