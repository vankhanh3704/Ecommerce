package com.hikiw.ecommerce.module.order.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.order.dto.CheckoutPreviewRequest;
import com.hikiw.ecommerce.module.order.dto.CheckoutPreviewResponse;
import com.hikiw.ecommerce.module.order.dto.OrderCreationRequest;
import com.hikiw.ecommerce.module.order.dto.OrderResponse;
import com.hikiw.ecommerce.module.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
