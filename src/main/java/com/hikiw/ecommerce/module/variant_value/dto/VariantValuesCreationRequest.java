package com.hikiw.ecommerce.module.variant_value.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValuesCreationRequest {

    Long variantId;
    String valueName;
    Integer displayOrder = 0; // Lower values indicate higher priority : hiển thị thứ tự ưu tiên
    Boolean isActive = true;
    String imageUrl;

}
