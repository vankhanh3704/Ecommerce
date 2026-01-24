package com.hikiw.ecommerce.Model.Request.variant.values;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantValuesUpdateRequest {
    String valueName;
    Integer displayOrder;
    Boolean isActive = true;
    String imageUrl;
}
