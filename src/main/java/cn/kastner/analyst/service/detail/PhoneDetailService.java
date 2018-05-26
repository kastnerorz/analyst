package cn.kastner.analyst.service.detail;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.detail.PhoneDetail;

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
     * @param phoneDetailId 手机详细信息对象 ID
     * @return phoneDetail retrieved
     */
    PhoneDetail findById(Long phoneDetailId);

    /**
     * retrieve all phoneDetails by Item
     *
     * @return 手机详细信息对象
     */
    PhoneDetail findByItem(Item item);

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
