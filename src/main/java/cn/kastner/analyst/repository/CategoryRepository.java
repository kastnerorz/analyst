package cn.kastner.analyst.repository;

import cn.kastner.analyst.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    Category findByLevelOneAndAndLevelTwoAndAndLevelThree(int levelOne, int levelTwo, int levelThree);
}
