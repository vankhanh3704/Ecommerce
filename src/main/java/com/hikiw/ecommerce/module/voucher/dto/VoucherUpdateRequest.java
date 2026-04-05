package com.hikiw.ecommerce.module.voucher.dto;


import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherUpdateRequest {
    Double minSpend;
    Double maxDiscount;
    Integer usageLimit;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean isActive;

    @Min(value = 1, message = "User usage limit must be at least 1")
    Integer userUsageLimit;
}
