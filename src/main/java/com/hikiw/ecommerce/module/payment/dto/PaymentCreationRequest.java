package com.hikiw.ecommerce.module.payment.dto;


import com.hikiw.ecommerce.Enum.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreationRequest {

    @NotEmpty(message = "Order IDs cannot be empty")
    List<Long> orderIds;

    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod;
}
