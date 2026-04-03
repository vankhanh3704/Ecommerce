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

    // Danh sách item được chọn
    List<CartItemResponse> selectedItems;

    // Tiền
    Double subtotal;
    Double shippingFee;
    Double discountAmount;
    Double totalAmount;

    // Voucher info (nếu có)
    String voucherCode;
    String voucherDescription;
    Boolean isFreeShipping;

    // Cảnh báo nếu có item hết hàng
    List<String> warnings;
}
