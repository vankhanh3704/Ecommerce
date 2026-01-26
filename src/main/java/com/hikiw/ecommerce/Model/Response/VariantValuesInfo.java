package com.hikiw.ecommerce.Model.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class VariantValuesInfo {
    Long variantValueId;
    Long variantId;
    String variantName;
    String valueName;
    String imageUrl;
}
