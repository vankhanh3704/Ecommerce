package com.hikiw.ecommerce.Model.Response;


import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class ProductVariantResponse {
    Long productVariantId;
    String sku;
    Long productId;
    Long productName;
    Double price;
    Integer stock;
    Double oldPrice;
    String imageUrl;
    Boolean isActive;
    Boolean inStock; // Trạng thái còn hàng hay hết hàng
    String variantInfo; // Thông tin biến thể dưới dạng chuỗi, ví dụ: "Màu: Đỏ, Kích cỡ: M"
    Double discountPercentage; // Tỷ lệ phần trăm giảm giá
    private List<ProductVariantMappingEntity> variantMappings = new ArrayList<>();




}
