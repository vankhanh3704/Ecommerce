package com.hikiw.ecommerce.module.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.role.entity.RoleEntity;
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherUsageEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;
    String password;

//    String fullName;
//    String email;
//    String phoneNumber;

    @ManyToMany
    Set<RoleEntity> roles;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    ShopEntity shop;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    CartEntity cart;

}
