package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.domain.Demand;
import cn.kastner.analyst.repository.core.DemandRepository;
import cn.kastner.analyst.service.core.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandServiceImpl implements DemandService {

    private final DemandRepository demandRepository;

    @Autowired
    public DemandServiceImpl(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }


    @Override
    public Demand insertByDemand(Demand demand) {
        return demandRepository.save(demand);
    }

    @Override
    public Demand findById(Long demandId) {
        return demandRepository.findDemandById(demandId);
    }

    @Override
    public List<Demand> findAllByCategory(Category category) {
        return demandRepository.findDemandsByCategory(category);
    }

    @Override
    public List<Demand> findByKeyword(Category category, String keyword) {
        return demandRepository.findDemandsByCategoryAndContentContaining(category, keyword);
    }

    @Override
    public Demand findByContent(String content) {
        return demandRepository.findDemandByContent(content);
    }

    @Override
    public List<Demand> findAll() {
        return demandRepository.findAll();
    }

    @Override
    public Demand update(Demand demand) {
        return demandRepository.save(demand);
    }

    @Override
    public Demand delete(Long demandId) {
        Demand demand = demandRepository.findDemandById(demandId);
        demandRepository.delete(demand);
        return demand;
    }
}
