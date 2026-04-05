package com.hikiw.ecommerce.module.review.repository;

import com.hikiw.ecommerce.module.review.entity.ReviewHelpfulEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewHelpfulRepository extends JpaRepository<ReviewHelpfulEntity, Long> {
    Optional<ReviewHelpfulEntity> findByReview_ReviewIdAndUser_Id(Long reviewId, Long userId);
}