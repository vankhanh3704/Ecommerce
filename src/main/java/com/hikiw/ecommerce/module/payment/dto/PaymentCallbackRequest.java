package com.hikiw.ecommerce.module.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCallbackRequest {
    String referenceCode;
    String transactionId;
    Boolean isSuccess;
    String message;
    String rawResponse;

    // THÊM TRƯỜNG NÀY ĐỂ BẢO MẬT
    @NotBlank(message = "Secure Hash is required")
    String secureHash;
}
