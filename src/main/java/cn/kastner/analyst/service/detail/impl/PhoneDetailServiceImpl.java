package cn.kastner.analyst.service.detail.impl;

import cn.kastner.analyst.domain.detail.PhoneDetail;
import cn.kastner.analyst.repository.detail.PhoneDetailRepository;
import cn.kastner.analyst.service.detail.PhoneDetailService;
import org.springframework.stereotype.Service;

@Service
public class PhoneDetailServiceImpl implements PhoneDetailService {

    private final PhoneDetailRepository phoneDetailRepository;

    public PhoneDetailServiceImpl(PhoneDetailRepository phoneDetailRepository) {
        this.phoneDetailRepository = phoneDetailRepository;
    }


}
