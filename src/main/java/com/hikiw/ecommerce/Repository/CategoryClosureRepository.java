package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.CategoryClosureEntity;
import com.hikiw.ecommerce.constant.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, CategoryClosureId> {
    List<CategoryClosureEntity> findAllByDescendant_CategoryId(Long descendantId);
    List<CategoryClosureEntity> findAllByAncestor_CategoryId(Long ancestorId);
}
