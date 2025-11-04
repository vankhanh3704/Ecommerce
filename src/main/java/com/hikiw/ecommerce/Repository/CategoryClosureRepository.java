package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.constant.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryRepository, CategoryClosureId> {
}
