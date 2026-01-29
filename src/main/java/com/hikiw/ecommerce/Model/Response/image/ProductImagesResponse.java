package com.hikiw.ecommerce.Model.Response.image;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductImagesResponse {
    Long productId;
    String productName;
    Integer totalImages;
    ProductImageResponse primaryImage;
    List<ProductImageResponse> images;
}
