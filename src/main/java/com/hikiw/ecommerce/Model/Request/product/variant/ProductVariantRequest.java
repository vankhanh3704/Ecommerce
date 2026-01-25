package com.hikiw.ecommerce.Model.Request.product.variant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantRequest {
    Long productId;
    String sku;
    Double price;
    Double oldPrice;
    Integer stock;
    String imageUrl;
    Boolean isActive;
}
