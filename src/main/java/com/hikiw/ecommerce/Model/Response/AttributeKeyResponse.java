package com.hikiw.ecommerce.Model.Response;


import com.hikiw.ecommerce.Enum.Datatype;
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
    Datatype datatype;
    String description;
    Integer displayOrder;
}
