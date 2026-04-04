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

    // User info
    Long userId;
    String username;

    // Shop info
    Long shopId;
    String shopName;

    // Shipping info
    String receiverName;
    String receiverPhone;
    String shippingAddress;

    // --- MONEY & VOUCHER DETAILS CỦA RIÊNG SHOP NÀY ---
    Double subtotal;                // Tiền hàng của riêng shop này

    Double originalShippingFee;     // Phí ship gốc của gói hàng này
    Double shippingDiscount;        // Số tiền ship được giảm
    Double shippingFee;             // Phí ship sau cùng (original - discount)

    String shopVoucherCode;         // Mã giảm giá của Shop
    Double shopDiscountAmount;      // Số tiền Shop giảm

    String platformVoucherCode;     // Mã giảm giá của Sàn (nếu có áp dụng cho đơn này)
    Double platformDiscountAmount;  // Số tiền Sàn giảm phân bổ cho đơn này

    Double totalAmount;             // = subtotal + shippingFee - shopDiscount - platformDiscount

    // Status
    OrderStatus orderStatus;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;

    String note;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;

    // Items & History
    List<OrderItemResponse> orderItems;
    List<OrderStatusHistoryResponse> statusHistory;
}