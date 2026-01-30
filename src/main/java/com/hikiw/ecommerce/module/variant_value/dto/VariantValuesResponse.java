package com.hikiw.ecommerce.module.variant_value.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VariantValuesResponse {
    Long variantValueId;
    Long variantId;
    String variantName;
    String valueName;
    Integer displayOrder;
    Boolean isActive;
    String imageUrl;

}
