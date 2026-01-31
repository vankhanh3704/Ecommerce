package com.hikiw.ecommerce.module.cart.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartSummaryResponse {
    Integer totalItems;
    Double totalPrice;
    Double estimatedShipping;
    Double estimatedTax;
    Double total;
}
