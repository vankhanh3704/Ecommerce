package com.hikiw.ecommerce.module.order.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {

    Long orderItemId;
    Long productVariantId;
    String productName;
    String sku;
    String variantInfo;
    String imageUrl;
    Double pricePerUnit; // Giá của một đơn vị sản phẩm (đã áp dụng giảm giá nếu có)
    Integer quantity;
    Double totalPrice;
}
