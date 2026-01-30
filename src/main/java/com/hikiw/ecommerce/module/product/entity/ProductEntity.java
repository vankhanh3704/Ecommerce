package com.hikiw.ecommerce.module.product.entity;


import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.shop_location.entity.ShopLocationEntity;
import com.hikiw.ecommerce.module.category.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

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
}
