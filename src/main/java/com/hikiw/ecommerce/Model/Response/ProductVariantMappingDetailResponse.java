package com.hikiw.ecommerce.Model.Response;


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
public class ProductVariantMappingDetailResponse {
    Long mappingId;

    Long productVariantId;
    String productVariantSku;
    Double price;
    Integer stock;

    Long variantValueId;
    Long variantId;
    String variantName;
    String valueName;
    String valueImageUrl;
    Integer valueDisplayOrder;

    String fullInfo;
}
