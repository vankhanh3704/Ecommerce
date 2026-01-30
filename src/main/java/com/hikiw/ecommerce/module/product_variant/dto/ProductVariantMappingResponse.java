package com.hikiw.ecommerce.module.product_variant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class ProductVariantMappingResponse {
    Long mappingId;
    Long productVariantId;
    String productVariantSku;
    Long variantValueId;
    String variantName;
    String valueName;
    String valueImageUrl;
    String fullInfo;
}
