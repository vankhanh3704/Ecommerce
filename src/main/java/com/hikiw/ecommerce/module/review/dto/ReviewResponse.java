package com.hikiw.ecommerce.module.review.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    Long reviewId;

    // User info (ẩn nếu isAnonymous = true)
    Long userId;
    String username;

    // Product info
    Long productId;
    String productName;

    // Order item info
    Long orderItemId;
    String variantInfo; // VD: "Đỏ - M"

    // Review content
    Integer rating;
    String comment;
    List<String> imageUrls;
    Boolean isAnonymous;
    Integer helpfulCount;

    // Reply từ shop
    ReviewReplyResponse reply;

    LocalDateTime createdDate;
    LocalDateTime updatedDate;
}