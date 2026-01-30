package com.hikiw.ecommerce.module.image.entity;

import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "product_image")
// MỘT sản phẩm có NHIỀU hình ảnh
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product;

    String imageUrl;
    Boolean isPrimary = false;
    Integer displayOrder = 0;
    String altText; // mô tả ảnh
    Long imageSize; // Kích thước file (bytes)
    Integer imageWidth;  // Chiều rộng ảnh (px)
    Integer imageHeight;  // Chiều cao ảnh (px)
    String cloudinaryPublicId;  // Lưu public_id để dễ xóa

}
