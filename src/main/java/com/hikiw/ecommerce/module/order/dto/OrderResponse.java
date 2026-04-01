package com.hikiw.ecommerce.module.order.dto;


import com.hikiw.ecommerce.Enum.OrderStatus;
import com.hikiw.ecommerce.Enum.PaymentMethod;
import com.hikiw.ecommerce.Enum.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long orderId;
    String orderCode;

    Long userId;
    String username;

    // Shipping info
    String receiverName;
    String receiverPhone;
    String shippingAddress;

    // Voucher
    String voucherCode;

    // Money
    Double subtotal;
    Double shippingFee;
    Double discountAmount;
    Double totalAmount;

    // Status
    OrderStatus orderStatus;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;

    String note;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;

    List<OrderItemResponse> orderItems;
    List<OrderStatusHistoryResponse> statusHistory;
}
