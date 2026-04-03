package com.hikiw.ecommerce.module.order.dto;





import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
// bước preview trước khi đặt hàng
public class CheckoutPreviewRequest {
    @NotEmpty(message = "Please select at least one item")
    List<Long> selectedCartItemIds;

    String voucherCode; // Nullable — preview tính giá khi nhập voucher
}
