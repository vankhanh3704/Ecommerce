package com.hikiw.ecommerce.module.order.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.order.dto.*;
import com.hikiw.ecommerce.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OrderController {
    OrderService orderService;
    SecurityUtil securityUtil;


    // Bước 1: User chọn item → preview checkout
    @PostMapping("/checkout/preview")
    public ApiResponse<CheckoutPreviewResponse> previewCheckout(
            @Valid @RequestBody CheckoutPreviewRequest request) {
        return ApiResponse.<CheckoutPreviewResponse>builder()
                .result(orderService.previewCheckout(request,securityUtil.getCurrentUserId()))
                .build();
    }

    @PostMapping
    public ApiResponse<CheckoutResponse> createOrder(
            @Valid @RequestBody OrderCreationRequest request) {
        return ApiResponse.<CheckoutResponse>builder()
                .result(orderService.createOrder(request, securityUtil.getCurrentUserId()))
                .message("Orders created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getUserOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getMyOrders(securityUtil.getCurrentUserId()))
                .build();
    }
}
