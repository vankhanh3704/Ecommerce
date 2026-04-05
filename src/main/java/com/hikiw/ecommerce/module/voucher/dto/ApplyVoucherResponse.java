package com.hikiw.ecommerce.module.voucher.dto;


import com.hikiw.ecommerce.Enum.DiscountType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyVoucherResponse {
    Long voucherId;
    String code;
    DiscountType discountType;
    Double discountValue;
    Double orderAmount;      // Tiền hàng gốc
    Double discountAmount;   // Số tiền được giảm
    Double finalAmount;      // Tiền sau giảm
    Boolean isFreeShipping;  // Có miễn phí ship không
    Long shopId;
}
