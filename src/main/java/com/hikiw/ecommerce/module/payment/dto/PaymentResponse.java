package com.hikiw.ecommerce.module.payment.dto;


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
public class PaymentResponse {

    Long paymentId;

    // THAY THẾ orderId và orderCode cũ bằng mảng
    List<Long> orderIds;
    List<String> orderCodes;

    PaymentMethod paymentMethod;
    Double amount;
    String transactionId;
    String referenceCode;
    PaymentStatus paymentStatus;
    LocalDateTime paymentDate;
    String gatewayMessage;
    LocalDateTime createdDate;
    String paymentUrl; // URL redirect đến cổng thanh toán (nếu có)
}
