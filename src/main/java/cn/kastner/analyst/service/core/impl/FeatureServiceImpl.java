package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Feature;
import cn.kastner.analyst.repository.FeatureRepository;
import cn.kastner.analyst.service.core.FeatureService;

import java.util.List;

public class FeatureServiceImpl implements FeatureService {

    final private FeatureRepository featureRepository;

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
