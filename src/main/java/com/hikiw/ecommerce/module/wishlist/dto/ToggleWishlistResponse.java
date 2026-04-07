package com.hikiw.ecommerce.module.wishlist.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ToggleWishlistResponse {
    Long productId;
    Boolean isWishListed;  // true = đã thêm, false = đã xóa
    String message;
}