package com.hikiw.ecommerce.module.payment.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.payment.dto.PaymentCallbackRequest;
import com.hikiw.ecommerce.module.payment.dto.PaymentCreationRequest;
import com.hikiw.ecommerce.module.payment.dto.PaymentResponse;
import com.hikiw.ecommerce.module.payment.service.PaymentService;
import jakarta.annotation.Resource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    PaymentService paymentService;

    @PostMapping
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentCreationRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.createPayment(request))
                .message("Payment created successfully")
                .build();
    }

    // Webhook — cổng thanh toán gọi về sau khi user thanh toán
    @PostMapping("/callback")
    public ApiResponse<PaymentResponse> handleCallback(
            @RequestBody PaymentCallbackRequest request) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.handleCallback(request))
                .build();
    }

    @GetMapping("/order/{orderId}")
    public ApiResponse<PaymentResponse> getPaymentByOrderId(
            @PathVariable Long orderId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentByOrderId(orderId))
                .build();
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId) {
        return ApiResponse.<PaymentResponse>builder()
                .result(paymentService.getPaymentById(paymentId))
                .build();
    }
}
