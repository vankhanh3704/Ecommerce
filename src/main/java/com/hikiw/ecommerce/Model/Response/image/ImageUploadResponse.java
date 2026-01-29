package com.hikiw.ecommerce.Model.Response.image;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUploadResponse {
    Integer imageId;
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
