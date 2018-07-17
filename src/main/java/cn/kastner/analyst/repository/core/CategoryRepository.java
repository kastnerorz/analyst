package cn.kastner.analyst.repository.core;

import cn.kastner.analyst.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    Category findByCategoryId(Long categoryId);
    Category findByLevelOneAndAndLevelTwoAndAndLevelThree(int level1, int level2, int level3);
    Category findByLevelThreeName(String name);
    Category findByLevelOneNameOrLevelTwoNameOrLevelThreeName(String name1,String name2,String name3);
}
