package com.hikiw.ecommerce.module.review.repository;

import com.hikiw.ecommerce.module.review.entity.ReviewReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReplyRepository extends JpaRepository<ReviewReplyEntity, Long> {
    boolean existsByReview_ReviewId(Long reviewId);
}