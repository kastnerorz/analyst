package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Brand;
import cn.kastner.analyst.repository.BrandRepository;
import cn.kastner.analyst.service.core.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    final private
    BrandRepository brandRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand insertByBrand(Brand brand) {
        return null;
    }

    @Override
    public Brand delete(Long brandId) {
        return null;
    }

    @Override
    public Brand update(Long brandId) {
        return null;
    }

    @Override
    public Brand findById(Long brandId) {
        return null;
    }

    @Override
    public List<Brand> findAll() {
        return null;
    }
}
