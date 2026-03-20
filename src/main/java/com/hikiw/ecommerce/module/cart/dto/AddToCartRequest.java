package com.hikiw.ecommerce.module.cart.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {
//    Long userId; // không cần : sẽ theo tài khoản đăng nhập
    @NotNull(message = "Product variant Id is required")
    Long productVariantId;
    @Min(value = 1, message = "Quantity must be at least 1")
            @Max(value = 100, message = "Quantity cannot exceed 100")
    Integer quantity;

}
