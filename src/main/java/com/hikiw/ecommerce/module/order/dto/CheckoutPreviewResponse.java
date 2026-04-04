package com.hikiw.ecommerce.module.order.dto;

import com.hikiw.ecommerce.module.cart.dto.CartItemResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CheckoutPreviewResponse {

    // Mỗi shop là 1 nhóm riêng
    List<ShopOrderPreview> shopOrders;

    // Tổng cộng toàn bộ
    Double totalSubtotal;
    Double totalShippingFee;
    Double totalDiscount;
    Double grandTotal;

    List<String> warnings;
}
