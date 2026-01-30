package com.hikiw.ecommerce.module.category.repository;

import com.hikiw.ecommerce.module.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByCategoryIdNotIn(List<Long> ids);
}
