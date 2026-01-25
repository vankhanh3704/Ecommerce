package com.hikiw.ecommerce.Model.Request.product.variant.mapping;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantMappingCreationRequest {
    Long productVariantId;
    Long variantValueId;
}
