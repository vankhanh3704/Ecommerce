package com.hikiw.ecommerce.module.review.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRatingResponse {
    Long productId;
    String productName;
    Double averageRating;      // VD: 4.5
    Integer totalReviews;
    Map<Integer, Long> ratingDistribution; // {5: 10, 4: 5, 3: 2, 2: 1, 1: 0}
    List<ReviewResponse> reviews;
}