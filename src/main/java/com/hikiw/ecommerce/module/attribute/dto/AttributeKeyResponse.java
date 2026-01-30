package com.hikiw.ecommerce.module.attribute.dto;


import com.hikiw.ecommerce.Enum.DataType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AttributeKeyResponse {
    Long attributeKeyId;
    String keyName;
    String displayName;
    Boolean isActive;
    Boolean isRequired;
    DataType dataType;
    String description;
    Integer displayOrder;
}
