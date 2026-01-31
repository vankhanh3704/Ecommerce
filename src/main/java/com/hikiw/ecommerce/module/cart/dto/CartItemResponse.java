package com.hikiw.ecommerce.module.cart.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long cartItemId;
    Long productVariantId;

    // Product info
    Long productId;
    String productName;
    String sku;
    String productImageUrl; // primary

    // variant info
    List<VariantInfo> variantValues;
    Double price;
    Double oldPrice;
    Integer quantity;
    Integer totalPrice;

    // stock info
    Integer availableStock;
    Boolean inStock;


}
