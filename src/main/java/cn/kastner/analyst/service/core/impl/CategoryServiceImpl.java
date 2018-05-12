package cn.kastner.analyst.service.core.impl;

import cn.kastner.analyst.domain.Category;
import cn.kastner.analyst.repository.CategoryRepository;
import cn.kastner.analyst.service.core.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

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
}
