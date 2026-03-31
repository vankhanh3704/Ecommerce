package com.hikiw.ecommerce.module.voucher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyVoucherRequest {
    @NotNull(message = "User ID is required")
    Long userId;

    @NotBlank(message = "Voucher code is required")
    String code;

    @NotNull(message = "Order amount is required")
    Double orderAmount; // tổng tiền hàng (chưa tính phí ship và chưa trừ giảm giá) mà khách đang định mua.
}
