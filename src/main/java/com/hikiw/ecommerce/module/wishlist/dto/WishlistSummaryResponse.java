package com.hikiw.ecommerce.module.wishlist.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistSummaryResponse {
    Long userId;
    Long totalItems;
    List<WishlistResponse> items;
}