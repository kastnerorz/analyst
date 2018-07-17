package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.domain.Param;

import java.util.List;

public interface ParamService {
    /**
     * create a param
     *
     * @param param
     * @return param
     */
    Param insertByParam(Param param);

    /**
     * retrieve a param
     *
     * @param paramId
     * @return param
     */
    Param findById(Long paramId);

    /**
     * retrieve all params
     *
     * @return param list
     */
    List<Param> findAll();

    /**
     * 根据需求查出所有参数
     * @param demand 需求
     * @return 参数列表
     */
    List<Param> findByDemand(Demand demand);


    /**
     * update a param
     *
     * @param param
     * @return param
     */
    Param update(Param param);

    /**
     * delete a param
     *
     * @param paramId
     * @return param
     */
    Param delete(Long paramId);
}
