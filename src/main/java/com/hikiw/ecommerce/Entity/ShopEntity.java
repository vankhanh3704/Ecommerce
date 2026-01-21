package com.hikiw.ecommerce.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "shop")

public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long shopId;

    @Column(unique = true, nullable = false)
    String shopName;

    @Column
    Double rating = 0.0;

    @Column(length = 20)
    String phoneNumber;

    String email;

    @Column(columnDefinition = "TEXT")
    String description;


    Boolean isActive = true;

    Boolean isVerified = false;


    @Column(name = "total_products")
    Integer totalProducts = 0;

    @Column(name = "total_orders")
    Integer totalOrders = 0;

    String address;

    // giả định 1 user chỉ có 1 shop
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id",nullable = false)
    UserEntity owner;

    // list of shop locations
    @OneToMany(mappedBy = "shop",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<ShopLocationEntity> shopLocations;

    // list of products
    @OneToMany(mappedBy = "shop",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    Set<ProductEntity> products;


}
