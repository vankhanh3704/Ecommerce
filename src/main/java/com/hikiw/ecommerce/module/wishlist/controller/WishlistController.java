package com.hikiw.ecommerce.module.wishlist.controller;

import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.wishlist.dto.ToggleWishlistResponse;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistRequest;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistResponse;
import com.hikiw.ecommerce.module.wishlist.service.WishlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {
    WishlistService wishlistService;
    SecurityUtil securityUtil;


    @PostMapping("/products/{productId}")
    public ApiResponse<WishlistResponse> addToWishlist(@PathVariable Long productId) {
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.addProductToWishlist(productId, securityUtil.getCurrentUserId() ))
                .message("Product added to wishlist")
                .build();
    }

    @DeleteMapping("/products/{productId}")
    public ApiResponse<String> removeFromWishlist(@PathVariable Long productId) {
        wishlistService.removeProductFromWishlist(productId, securityUtil.getCurrentUserId());
        return ApiResponse.<String>builder()
                .result("Product removed from wishlist")
                .build();
    }

    @PostMapping("/products/{productId}/toggle")
    public ApiResponse<ToggleWishlistResponse> toggleWishlist(@PathVariable Long productId) {
        boolean isWishlisted = wishlistService.toggleWishlist(productId, securityUtil.getCurrentUserId());
        return ApiResponse.<ToggleWishlistResponse>builder()
                .result(ToggleWishlistResponse.builder()
                        .productId(productId)
                        .isWishListed(isWishlisted)
                        .message(isWishlisted ? "Added to wishlist" : "Removed from wishlist")
                        .build())
                .build();
    }
}
