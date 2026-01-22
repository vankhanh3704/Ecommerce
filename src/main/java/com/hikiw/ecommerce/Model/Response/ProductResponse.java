package com.hikiw.ecommerce.Model.Response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long productId;
    String productName;
    String description;
    Double basePrice;
    Long shopId;
    String shopName;
    Long categoryId;
    String categoryName;
    Long shopLocationId;
    String shopLocationName;
    Long soldCount;
}
