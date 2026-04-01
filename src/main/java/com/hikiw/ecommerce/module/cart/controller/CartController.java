package com.hikiw.ecommerce.module.cart.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.cart.dto.AddToCartRequest;
import com.hikiw.ecommerce.module.cart.dto.CartResponse;
import com.hikiw.ecommerce.module.cart.dto.CartUpdateRequest;
import com.hikiw.ecommerce.module.cart.service.CartService;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {
    CartService cartService;
    private final UserRepository userRepository;
    SecurityUtil securityUtil;


    @PostMapping()
    public ApiResponse<CartResponse> addProductToCart(@RequestBody AddToCartRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addProductToCart(userId, request))
                .build();
    }

    @GetMapping
    public ApiResponse<CartResponse> getCart() {
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.getCart(userId))
                .build();
    }


    @PutMapping("/items/{cartItemId}")
    public ApiResponse<CartResponse> updateCartItem(@PathVariable Long cartItemId,@RequestBody CartUpdateRequest request){
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateCartItem(userId, cartItemId, request))
                .build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ApiResponse<CartResponse> removeCartItem(@PathVariable Long cartItemId){
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeCartItem(userId, cartItemId))
                .build();
    }

    @DeleteMapping("/items")
    public ApiResponse<CartResponse> clearCart(){
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartResponse>builder()
                .result(cartService.clearCart(userId))
                .build();
    }

    @PostMapping("/{userId}/validate")
    public ApiResponse<String> validateCart(@PathVariable Long userId) {
        cartService.validateCart(userId);
        return ApiResponse.<String>builder()
                .result("Cart is valid")
                .build();
    }
}
