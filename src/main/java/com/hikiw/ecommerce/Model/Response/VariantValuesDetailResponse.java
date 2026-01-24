package com.hikiw.ecommerce.Model.Response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValuesDetailResponse {
    Long variantValueId;
    Long variantId;
    String variantName;
    String valueName;
    Integer displayOrder;
    Boolean isActive;
    String imageUrl;
    Integer usageCount; // số lượng products variant sử dụng giá trị biến thể này
}
