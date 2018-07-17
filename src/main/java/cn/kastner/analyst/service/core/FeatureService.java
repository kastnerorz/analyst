package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Feature;

import java.util.List;

public interface FeatureService {

    /**
     * create a feature
     *
     * @param feature
     * @return feature inserted
     */
    Feature insertByFeature(Feature feature);

    /**
     * retrieve a feature by id
     *
     * @param featureId
     * @return feature retrieved
     */
    Feature findById(Long featureId);

    /**
     * retrieve all categories
     *
     * @return
     */
    List<Feature> findAll();

    /**
     * update a feature by id
     *
     * @param feature
     * @return
     */
    Feature update(Feature feature);

    /**
     * delete a feature by id
     *
     * @param featureId
     * @return feature deleted
     */
    Feature delete(Long featureId);
}
