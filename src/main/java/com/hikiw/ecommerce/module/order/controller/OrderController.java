package com.hikiw.ecommerce.module.order.controller;


import com.cloudinary.Api;
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

    @GetMapping("/my-orders")
    public ApiResponse<List<OrderResponse>> getUserOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getMyOrders(securityUtil.getCurrentUserId()))
                .build();
    }

    @GetMapping("/my-orders/{orderId}")
    public ApiResponse<OrderResponse> getOrderDetails(@PathVariable Long orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getMyOrderById(orderId, securityUtil.getCurrentUserId()))
                .message("Orders details successfully")
                .build();
    }



    // ========== ADMIN ENDPOINTS ==========

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders())
                .build();
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderById(orderId))
                .build();
    }

    @GetMapping("/code/{orderCode}")
    public ApiResponse<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOrderByCode(orderCode))
                .build();
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.updateOrderStatus(orderId, request))
                .message("Order status updated")
                .build();
    }

    @PutMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelByAdmin(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.cancelOrderByAdmin(orderId, reason))
                .message("Order cancelled")
                .build();
    }
    // User tự hủy đơn
    @PutMapping("/my-orders/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelMyOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason) {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.cancelMyOrder(orderId, reason, securityUtil.getCurrentUserId()))
                .message("Order cancelled successfully")
                .build();
    }
}
