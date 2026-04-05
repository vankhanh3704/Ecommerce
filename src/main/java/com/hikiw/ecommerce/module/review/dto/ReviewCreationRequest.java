package com.hikiw.ecommerce.module.review.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewCreationRequest {
    @NotNull(message = "Order item ID is required")
    Long orderItemId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    String comment;

    List<String> imageUrls; // Danh sách URL ảnh (upload trước rồi truyền URL vào)

    Boolean isAnonymous = false;
}
