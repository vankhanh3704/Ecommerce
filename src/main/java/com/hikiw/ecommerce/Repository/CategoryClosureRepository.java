package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.CategoryClosureEntity;
import com.hikiw.ecommerce.constant.CategoryClosureId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryClosureRepository extends JpaRepository<CategoryClosureEntity, CategoryClosureId> {
    List<CategoryClosureEntity> findAllByDescendant_CategoryId(Long descendantId);

    List<CategoryClosureEntity> findAllByDescendant_CategoryIdAndDepth(Long categoryId, Integer depth);

    List<CategoryClosureEntity> findAllByAncestor_CategoryId(Long ancestorId);

    List<CategoryClosureEntity> findAllByAncestor_CategoryIdAndDepth(Long categoryId, Integer depth);

    // delete
    void deleteAllByAncestor_CategoryId(Long categoryId);

    void deleteAllByDescendant_CategoryId(Long categoryId);

    // --- Phương thức Xóa Tối ưu cho chức năng MOVE (Nâng cao) ---
    @Modifying
    @Query(nativeQuery = true, value = """
                DELETE FROM category_closure 
                WHERE descendant_id IN (
                    SELECT * FROM (
                        SELECT descendant_id 
                        FROM category_closure 
                        WHERE ancestor_id = :categoryId
                    ) AS T1 -- Bảng tạm 1
                )
                AND ancestor_id IN (
                    SELECT * FROM (
                        SELECT ancestor_id 
                        FROM category_closure 
                        WHERE descendant_id = :categoryId
                    ) AS T2 -- Bảng tạm 2
                )
                AND ancestor_id <> :categoryId 
            """)
    void deleteOldPaths(@Param("categoryId") Long categoryId);

    @Modifying
    @Query(nativeQuery = true, value = """
                INSERT INTO category_closure (ancestor_id, descendant_id, depth)
                SELECT T1.ancestor_id, T2.descendant_id, T1.depth + T2.depth + 1
                FROM category_closure AS T1, category_closure AS T2
                WHERE T1.descendant_id = :newParentId
                  AND T2.ancestor_id = :categoryId
            """)
    void insertNewPaths(@Param("categoryId") Long categoryId, @Param("newParentId") Long newParentId);


    // Tìm ID của TẤT CẢ danh mục là hậu duệ (descendant) của một danh mục khác (ancestor)
    // và loại bỏ mối quan hệ tự liên kết (depth = 0)
    @Query("SELECT DISTINCT cc.descendant.categoryId FROM CategoryClosureEntity cc WHERE cc.depth > 0")
    List<Long> findAllNonRootIds();
}
