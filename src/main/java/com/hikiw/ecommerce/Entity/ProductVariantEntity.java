package com.hikiw.ecommerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "product_variant")
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_variant_id")
    Long productVariantId; // sản phẩm biến thể

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product; // sản phẩm gốc

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ProductVariantMappingEntity> variantMappings = new ArrayList<>(); // các biến thể của sản phẩm

    String sku; // mã SKU
    Double price; // Giá bán hiện tại (QUAN TRỌNG: Mỗi variant có giá riêng)
    Double oldPrice; // Giá cũ (để hiển thị khuyến mãi)
    Integer stock = 0; // số lượng tồn kho của biến thể
    Integer soldCount = 0; // số lượng đã bán của biến thể
    String imageUrl;
    Boolean isActive = true;



    // ========== HELPER METHODS ==========
    public Boolean getInStock() {
        return stock != null && stock > 0; // Còn hàng nếu số lượng tồn kho lớn hơn 0
    }


    // Phương thức để lấy thông tin biến thể dưới dạng chuỗi
    public String getVariantInfoString() {
        return variantMappings.stream()
                .map(mapping -> mapping.getVariantValue().getValueName())
                .reduce((a, b) -> a + " - " + b)
                .orElse("");
    }

    // Phương thức để tính toán tỷ lệ phần trăm giảm giá
    public Double getDiscountPercentage() {
        if(oldPrice != null && oldPrice > 0 && price != null){
            return ((oldPrice - price) / oldPrice) * 100;
        } else {
            return 0.0;
        }
    }
}
