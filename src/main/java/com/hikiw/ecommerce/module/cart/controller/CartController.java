package com.hikiw.ecommerce.module.cart.controller;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.service.CartService;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {
    CartService cartService;
    private final UserRepository userRepository;
    SecurityUtil securityUtil;

    @GetMapping()
    public ApiResponse<CartEntity> getOrCreateCart() {
        Long userId = securityUtil.getCurrentUserId();
        return ApiResponse.<CartEntity>builder()
                .result(cartService.getOrCreateCart(userId))
                .build();
    }


}
