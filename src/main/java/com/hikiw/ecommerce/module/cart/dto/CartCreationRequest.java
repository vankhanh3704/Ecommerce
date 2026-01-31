package com.hikiw.ecommerce.module.cart.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartCreationRequest {
//    Long userId; // không cần : sẽ theo tài khoản đăng nhập
    Long productVariantId;
    Integer quantity;

}
