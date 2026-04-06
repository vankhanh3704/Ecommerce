package com.hikiw.ecommerce.module.review.repository;

import com.hikiw.ecommerce.module.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    // Kiểm tra user đã review orderItem này chưa
    boolean existsByOrderItem_OrderItemId(Long orderItemId);

    List<ReviewEntity> findByUser_IdAndIsActiveTrueOrderByCreatedDateDesc(Long userId);

}
