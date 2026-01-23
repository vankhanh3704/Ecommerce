package com.hikiw.ecommerce.Model.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantResponse {
    Long variantId;
    String variantName;
    Integer displayOrder;
    Integer totalValues;
}
