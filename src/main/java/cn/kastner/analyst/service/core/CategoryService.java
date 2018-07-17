package cn.kastner.analyst.service.core;

import cn.kastner.analyst.domain.Category;

import java.util.List;

public interface CategoryService {

    /**
     * create a category
     *
     * @param category
     * @return category inserted
     */
    Category insertByCategory(Category category);

    /**
     * retrieve a category by id
     *
     * @param categoryId
     * @return category retrieved
     */
    Category findById(Long categoryId);
    /**
     * retrieve all categories
     *
     * @return
     */
    List<Category> findAll();

    /**
     * update a category by id
     *
     * @param category
     * @return
     */
    Category update(Category category);

    /**
     * delete a category by id
     *
     * @param categoryId
     * @return category deleted
     */
    Category delete(Long categoryId);

    Category findByLevels(int level1, int level2, int level3);

    Category findByLevelName(String name);
}
