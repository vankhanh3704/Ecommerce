package com.hikiw.ecommerce.module.wishlist.controller;

import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistCreationRequest;
import com.hikiw.ecommerce.module.wishlist.dto.WishlistResponse;
import com.hikiw.ecommerce.module.wishlist.service.WishlistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {
    WishlistService wishlistService;


    @PostMapping
    public ApiResponse<WishlistResponse> addToWishlist(@RequestBody WishlistCreationRequest request){
        return ApiResponse.<WishlistResponse>builder()
                .result(wishlistService.addProductToWishlist(request))
                .message("Product added to wishlist successfully")
                .build();
    }
}
