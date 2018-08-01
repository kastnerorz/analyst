package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Demand;

import java.util.List;

public interface DemandService {
    /**
     * create a demand
     *
     * @param demand
     * @return demand
     */
    Demand insertByDemand(Demand demand);

    /**
     * retrieve a demand
     *
     * @param demandId
     * @return demand
     */
    Demand findById(Long demandId);

    /**
     * 根据品类查出所有需求
     * @param category 品类
     * @return 需求列表
     */
    List<Demand> findAllByCategory(Category category);

    /**
     * 在当前品类下搜索需求
     * @param category 品类
     * @param keyword 搜索关键字
     * @return
     */
    List<Demand> findByKeyword(Category category, String keyword);

    Demand findByContent(String content);

    /**
     * retrieve all demands
     *
     * @return demand list
     */
    List<Demand> findAll();


    /**
     * update a demand
     *
     * @param demand
     * @return demand
     */
    Demand update(Demand demand);

    /**
     * delete a demand
     *
     * @param demandId
     * @return demand
     */
    Demand delete(Long demandId);
}
