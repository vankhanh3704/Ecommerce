package com.hikiw.ecommerce.module.order.dto;





import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// bước preview trước khi đặt hàng
public class CheckoutPreviewRequest {
    // Danh sách cartItemId được chọn để mua
    @NotEmpty(message = "Please select at least one item")
    List<Long> selectedCartItemIds;

    // Voucher theo từng shop
    // Key = shopId, Value = voucherCode
    // VD: { "1": "SHOPAVOUCHER20", "2": "SHOPBSALE10" }
    Map<Long, String> shopVoucherCodes;

    // Voucher toàn sàn (platform) — optional
    String platformVoucherCode;
}
