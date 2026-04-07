package com.hikiw.ecommerce.module.wishlist.repository;

import com.hikiw.ecommerce.module.wishlist.entity.WishlistEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    //Bắt buộc JOIN với product ngay từ đầu
    @EntityGraph(attributePaths = {"product"})
    List<WishlistEntity> findByUser_IdOrderByCreatedDateDesc(Long userId);

    Optional<WishlistEntity> findByUser_IdAndProduct_ProductId(Long userId, Long productId);

    boolean existsByUser_IdAndProduct_ProductId(Long userId, Long productId);

    // Xóa trực tiếp bằng SQL thay vì Select rồi mới Delete
    @Modifying
    @Transactional
    @Query("DELETE FROM WishlistEntity w WHERE w.user.id = :userId AND w.product.productId = :productId")
    void deleteByUser_IdAndProduct_ProductIdCustom(Long userId, Long productId);

    Long countByUser_Id(Long userId);
}
