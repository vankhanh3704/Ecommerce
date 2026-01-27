package com.hikiw.ecommerce.Model.Request.product.attribute;


import com.hikiw.ecommerce.Enum.Datatype;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttributeKeyUpdateRequest {
    String keyName;
    String displayName;
    Datatype datatype;
    Boolean isRequired;
    String description;
    Integer displayOrder;
    Boolean isActive;
}
