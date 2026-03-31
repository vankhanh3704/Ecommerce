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
}
