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
public class ShopOrderPreview {
    Long shopId;
    String shopName;

    // Items của shop này
    List<CartItemResponse> items;

    // Tiền của shop này
    Double subtotal;
    Double shippingFee;
    Double discountAmount;
    Double totalAmount;

    // Voucher đang áp dụng
    String voucherCode;
    String voucherDescription;
    Boolean isFreeShipping;
}
