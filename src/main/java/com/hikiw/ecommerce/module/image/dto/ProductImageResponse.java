package com.hikiw.ecommerce.module.image.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductImageResponse {
    Long productId;
    Long imageId;
    String imageUrl;
    String cloudinaryPublicId;
    Boolean isPrimary;
    Integer displayOrder;
    String altText; // mô tả ảnh
    Long imageSize; // Kích thước file (bytes)
    Integer imageWidth;  // Chiều rộng ảnh (px)
    Integer imageHeight;

    // Cloudinary transformations
    String thumbnailUrl;  // 200x200
    String mediumUrl;     // 800x600
}
