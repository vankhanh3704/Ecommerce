package com.hikiw.ecommerce.module.product.entity;


import com.hikiw.ecommerce.module.image.entity.ProductImageEntity;
import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.shop_location.entity.ShopLocationEntity;
import com.hikiw.ecommerce.module.category.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long productId;

    String productName;

    @Column(columnDefinition = "TEXT")
    String description;

    Double basePrice;

    @ManyToOne
    @JoinColumn(name = "shop_id",nullable = false)
    ShopEntity shop;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_location_id")
    private ShopLocationEntity shopLocation;
    Long soldCount = 0L;
    Boolean isActive = true;


    // Trong ProductEntity.java — thêm 2 field này nếu chưa có

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    List<ProductVariantEntity> variants = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Builder.Default
    List<ProductImageEntity> images = new ArrayList<>();
}
