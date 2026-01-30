package com.hikiw.ecommerce.module.product_variant.dto;


import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantDetailResponse {
    Long productVariantId;
    String sku;
    Long productId;
    String productName;
    Double price;
    Integer stock;
    Double oldPrice;
    String imageUrl;
    Boolean isActive;
    Boolean inStock; // Trạng thái còn hàng hay hết hàng
    Double discountPercentage; // Tỷ lệ phần trăm giảm giá
    List<VariantValuesInfo> variantValues; // Danh sách các giá trị biến thể

}
