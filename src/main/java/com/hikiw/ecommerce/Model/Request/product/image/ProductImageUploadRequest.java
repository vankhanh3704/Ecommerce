package com.hikiw.ecommerce.Model.Request.product.image;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductImageUploadRequest {

    Long productId;
    MultipartFile imageFile;
    Boolean isPrimary;
    String altText;

}
