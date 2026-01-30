package com.hikiw.ecommerce.module.attribute.dto;


import com.hikiw.ecommerce.Enum.DataType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeKeyCreationRequest {
    String keyName;
    String displayName;
    DataType dataType;
    Boolean isRequired;
    String description;
    Integer displayOrder;
}
