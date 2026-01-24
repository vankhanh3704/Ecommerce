package com.hikiw.ecommerce.Model.Response;


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
