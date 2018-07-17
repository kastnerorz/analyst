package cn.kastner.analyst.service.detail.impl;

import cn.kastner.analyst.domain.Item;
import cn.kastner.analyst.domain.PhoneDetail;
import cn.kastner.analyst.repository.detail.PhoneDetailRepository;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手机详细信息服务实现
 */
@Service
public class PhoneDetailServiceImpl implements PhoneDetailService {

    private final PhoneDetailRepository phoneDetailRepository;

    @Autowired
    public PhoneDetailServiceImpl(PhoneDetailRepository phoneDetailRepository) {
        this.phoneDetailRepository = phoneDetailRepository;
    }


    @Override
    public PhoneDetail insertByPhoneDetail(PhoneDetail phoneDetail) {
        return phoneDetailRepository.save(phoneDetail);
    }

    @Override
    public PhoneDetail findById(Long id) {
        return phoneDetailRepository.findByid(id);
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
    public PhoneDetail delete(Long id) {
        PhoneDetail phoneDetail = phoneDetailRepository.findByid(id);
        phoneDetailRepository.delete(phoneDetail);
        return phoneDetail;
    }
}
