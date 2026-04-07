package com.hikiw.ecommerce.module.wishlist.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistResponse {
    Long wishlistId;

    // Product info
    Long productId;
    String productName;
    String productImageUrl;
    Double basePrice;
    Boolean isActive;

    // Giá thấp nhất trong các variant
    Double minPrice;
    Double maxPrice;

    Boolean isInStock;

    LocalDateTime addedDate;
}
