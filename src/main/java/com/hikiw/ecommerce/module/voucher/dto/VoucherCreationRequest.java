package com.hikiw.ecommerce.module.voucher.dto;


import com.hikiw.ecommerce.Enum.DiscountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherCreationRequest {
    @NotBlank(message = "Voucher code is required")
    String code;

    @NotNull(message = "Discount type is required")
    DiscountType discountType;

    @NotNull
    @Min(value = 0, message = "Discount value must be >= 0")
    Double discountValue;


    Double minSpend = 0.0;
    Double maxDiscount;
    Integer usageLimit;

    @NotNull(message = "Start date is required")
    LocalDateTime startDate;

    @NotNull(message = "End date is required")
    LocalDateTime endDate;

    @Min(value = 1, message = "User usage limit must be at least 1")
    Integer userUsageLimit = 1; // Giới hạn số lần dùng trên 1 khách hàng (Mặc định là 1)

    Long shopId; // Để biết Voucher này thuộc về Shop nào (Null nếu là Voucher toàn sàn)
}
