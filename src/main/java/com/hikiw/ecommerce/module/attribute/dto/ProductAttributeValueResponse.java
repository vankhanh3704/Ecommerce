package com.hikiw.ecommerce.module.attribute.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductAttributeValueResponse {
    Long pavId;
    Long productId;
    String productName;
    Long attributeKeyId;
    String attributeKeyName;
    String displayName;
    String dataType;
    String valueText;


}
