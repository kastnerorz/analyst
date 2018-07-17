package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Feature;
import cn.kastner.analyst.repository.core.FeatureRepository;
import cn.kastner.analyst.service.core.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    @Autowired
    public FeatureServiceImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public Feature insertByFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    @Override
    public Feature findById(Long featureId) {
        return featureRepository.findByFeatureId(featureId);
    }

    @Override
    public List<Feature> findAll() {
        return featureRepository.findAll();
    }

    @Override
    public Feature update(Feature feature) {
        return featureRepository.save(feature);
    }

    @Override
    public Feature delete(Long featureId) {
        Feature feature = featureRepository.findByFeatureId(featureId);
        featureRepository.delete(feature);
        return feature;
    }
}
