package com.hikiw.ecommerce.module.order.dto;

import com.hikiw.ecommerce.Enum.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentSummaryResponse {

    Double totalItemsSubtotal;      // Tổng tiền hàng của tất cả các shop

    Double totalOriginalShippingFee;// Tổng phí ship gốc
    Double totalShippingDiscount;   // Tổng tiền được giảm phí ship (từ mã Freeship)
    Double finalShippingFee;        // Phí ship thực tế phải trả

    Double totalShopDiscount;       // Tổng tiền giảm từ các Voucher của Shop
    Double totalPlatformDiscount;   // Tổng tiền giảm từ Voucher của Sàn

    Double grandTotal;              // TỔNG THANH TOÁN CUỐI CÙNG (Số tiền user phải trả)

    PaymentMethod paymentMethod;    // Phương thức thanh toán chung
}