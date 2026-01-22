package com.hikiw.ecommerce.Model.Request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    Long categoryId;
    Long shopLocationId;
    String productName;
    String description;
    Double basePrice;
    Boolean isActive;

}
