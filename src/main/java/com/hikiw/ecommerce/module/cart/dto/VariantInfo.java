package com.hikiw.ecommerce.module.cart.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VariantInfo {
    String variantName;
    String valueName;
    String imageUrl;
}
