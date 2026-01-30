package com.hikiw.ecommerce.module.attribute.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributeValueCreationRequest {
    Long attributeKeyId;
    Long productId;
    String valueText; // For text-based attributes
}
