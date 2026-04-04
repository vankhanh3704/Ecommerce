package com.hikiw.ecommerce.module.order.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckoutResponse {

    String checkoutSessionId; // Mã phiên giao dịch (Tùy chọn, dùng để trace log hoặc gửi sang cổng thanh toán)

    // Danh sách các đơn hàng được tách theo từng Shop
    List<OrderResponse> orders;

    // Cục tổng tiền để hiển thị lên UI và gửi cho VNPay/Momo
    PaymentSummaryResponse paymentSummary;
}
