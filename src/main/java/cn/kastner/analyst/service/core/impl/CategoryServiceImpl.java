package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.repository.core.CategoryRepository;
import cn.kastner.analyst.service.core.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category insertByCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category update(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category delete(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        categoryRepository.delete(category);
        return category;
    }

    @Override
    public Category findByLevels(int level1, int level2, int level3) {
        return categoryRepository.findByLevelOneAndAndLevelTwoAndAndLevelThree(level1, level2, level3);
    }

    @Override
    public Category findByLevelName(String name){
        return categoryRepository.findByLevelThreeName(name);
    }



}
