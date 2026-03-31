package com.hikiw.ecommerce.module.voucher.dto;


import com.hikiw.ecommerce.Enum.DiscountType;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class
VoucherResponse {
    String voucherId;
    String code;
    DiscountType discountType;
    Double discountValue;
    Double minSpend;
    Double maxDiscount;
    Integer usageLimit;
    Integer usedCount;
    LocalDateTime startDate;
    LocalDateTime endDate;
    Boolean isActive;

    Boolean isValid; // check xem còn hiệu lực hay không ( dựa vào ngày hết hạn và số lần sử dụng đã dùng)


}
