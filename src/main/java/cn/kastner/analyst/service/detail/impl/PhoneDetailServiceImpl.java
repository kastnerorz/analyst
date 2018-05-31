package cn.kastner.analyst.service.detail.impl;

import cn.kastner.analyst.domain.core.Item;
import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.repository.detail.PhoneDetailRepository;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手机详细信息服务实现
 */
@Service
public class PhoneDetailServiceImpl implements PhoneDetailService {

    private final PhoneDetailRepository phoneDetailRepository;

    public PhoneDetailServiceImpl(PhoneDetailRepository phoneDetailRepository) {
        this.phoneDetailRepository = phoneDetailRepository;
    }


    @Override
    public PhoneDetail insertByPhoneDetail(PhoneDetail phoneDetail) {
        return phoneDetailRepository.save(phoneDetail);
    }

    @Override
    public PhoneDetail findById(Long phoneDetailId) {
        return phoneDetailRepository.findByPhoneDetailId(phoneDetailId);
    }

    @Override
    public PhoneDetail findByItem(Item item) {
        return phoneDetailRepository.findByItem(item);
    }

    @Override
    public List<PhoneDetail> findByItemFilter(List<Integer> batteryCap, List<Double> cpuClock, List<Double> romCapacity, List<Double> ramCapacity, List<Integer> pxDensity) {
        return phoneDetailRepository.findByBatteryCapInAndCpuClockInAndRomCapacityInAndRamCapacityInAndPxDensityIn(batteryCap, cpuClock, romCapacity, ramCapacity, pxDensity);
    }

    @Override
    public PhoneDetail update(PhoneDetail phoneDetail) {
        return phoneDetailRepository.save(phoneDetail);
    }

    @Override
    public PhoneDetail delete(Long phoneDetailId) {
        PhoneDetail phoneDetail = phoneDetailRepository.findByPhoneDetailId(phoneDetailId);
        phoneDetailRepository.delete(phoneDetail);
        return phoneDetail;
    }
}
