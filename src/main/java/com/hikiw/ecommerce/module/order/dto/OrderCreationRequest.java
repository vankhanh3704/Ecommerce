package com.hikiw.ecommerce.module.order.dto;


import com.hikiw.ecommerce.Enum.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreationRequest {

    // để tạo đơn hàng, cần biết user nào đang đặt, nên cần userId. Sau này có thể lấy thông tin user từ token, nhưng tạm thời để đơn giản thì cứ truyền userId vào đây.
    @NotNull(message = "User ID is required")
    Long userId;

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
