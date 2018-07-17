package cn.kastner.analyst.service.detail;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.PhoneDetail;

import java.util.List;

/**
 * 手机详细信息服务接口
 */
public interface PhoneDetailService {

    /**
     * create a phoneDetail
     *
     * @param phoneDetail 手机详细信息对象
     * @return phoneDetail inserted
     */
    PhoneDetail insertByPhoneDetail(PhoneDetail phoneDetail);

    /**
     * retrieve a phoneDetail by id
     *
     * @param id 手机详细信息对象 ID
     * @return phoneDetail retrieved
     */
    PhoneDetail findById(Long id);


    /**
     * @param batteryCap
     * @param cpuClock
     * @param romCapacity
     * @param ramCapacity
     * @param pxDensity
     * @return
     */
    List<PhoneDetail> findByItemFilter(List<Integer> batteryCap,
                                       List<Double> cpuClock,
                                       List<Double> romCapacity,
                                       List<Double> ramCapacity,
                                       List<Integer> pxDensity);

    /**
     * update a phoneDetail by id
     *
     * @param phoneDetail 手机详细信息对象
     * @return 手机详细信息对象
     */
    PhoneDetail update(PhoneDetail phoneDetail);

    /**
     * delete a phoneDetail by id
     *
     * @param phoneDetailId 手机详细信息对象 ID
     * @return phoneDetail deleted
     */
    PhoneDetail delete(Long phoneDetailId);
}
