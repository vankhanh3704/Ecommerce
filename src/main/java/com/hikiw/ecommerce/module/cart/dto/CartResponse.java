package com.hikiw.ecommerce.module.cart.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    Long cartId;
    Long userId;
    Long totalItems;
    Double totalAmount;
    List<CartItemResponse> items;



}
