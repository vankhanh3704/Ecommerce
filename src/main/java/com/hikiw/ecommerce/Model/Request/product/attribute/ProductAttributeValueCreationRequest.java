package com.hikiw.ecommerce.Model.Request.product.attribute;


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
    String valueText;
}
