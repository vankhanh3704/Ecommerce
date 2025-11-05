package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.CategoryClosureEntity;
import com.hikiw.ecommerce.constant.CategoryClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, CategoryClosureId> {
    List<CategoryClosureEntity> findAllByDescendant_CategoryId(Long descendantId);
    List<CategoryClosureEntity> findAllByDescendant_CategoryIdAndDepth(Long categoryId, Integer depth);
    List<CategoryClosureEntity> findAllByAncestor_CategoryId(Long ancestorId);
    List<CategoryClosureEntity>findAllByAncestor_CategoryIdAndDepth(Long categoryId, Integer depth);

    // delete
    void deleteAllByAncestor_CategoryId(Long categoryId);
    void deleteAllByDescendant_CategoryId(Long categoryId);
}
