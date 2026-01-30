package com.hikiw.ecommerce.module.variant.dto;


import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VariantDetailResponse {
    Long variantId;
    String variantName;
    Integer displayOrder;
    Integer totalValues;
    List<VariantValuesResponse> variantValues;
}
