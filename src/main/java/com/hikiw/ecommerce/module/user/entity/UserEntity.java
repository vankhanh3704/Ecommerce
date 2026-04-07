package com.hikiw.ecommerce.module.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hikiw.ecommerce.Enum.Gender;
import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.role.entity.RoleEntity;
import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherEntity;
import com.hikiw.ecommerce.module.voucher.entity.VoucherUsageEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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

    @Column(unique = true, nullable = false)
    String username;
    @Column(nullable = false)
    String password;

    // --- THÔNG TIN CÁ NHÂN ---
    @Column(name = "full_name")
    String fullName;

    @Column(unique = true)
    String email;

    @Column(name = "phone_number", unique = true, length = 20)
    String phoneNumber;

    @Column(name = "avatar_url")
    String avatarUrl;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @ManyToMany
    Set<RoleEntity> roles;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    ShopEntity shop;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    CartEntity cart;

    

}
