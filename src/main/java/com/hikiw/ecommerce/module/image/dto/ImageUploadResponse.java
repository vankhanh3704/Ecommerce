package com.hikiw.ecommerce.module.image.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ImageUploadResponse {
    Long imageId;
    String imageUrl;
    String cloudinaryPublicId;
    String fileName;
    Long fileSize;
    Integer width;
    Integer height;
    String contentType;
    Boolean isPrimary;

    // Cloudinary transformations
    String thumbnailUrl;
    String mediumUrl;
}
