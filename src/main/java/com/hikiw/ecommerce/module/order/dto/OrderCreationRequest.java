package com.hikiw.ecommerce.module.order.dto;


import com.hikiw.ecommerce.Enum.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
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
public class OrderCreationRequest {

    // Không phải toàn bộ giỏ hàng
    @NotEmpty(message = "Please select at least one item")
    List<Long> selectedCartItemIds;

    @NotBlank(message = "Receiver name is required")
    String receiverName;

    @NotBlank(message = "Receiver phone is required")
    String receiverPhone;

    @NotBlank(message = "Shipping address is required")
    String shippingAddress;

    @NotNull(message = "Payment method is required")
    PaymentMethod paymentMethod;

    String voucherCode; // Mã voucher có thể có hoặc không, nên không bắt buộc

    String note; // Ghi chú đơn hàng (nếu có)
}
