package com.hikiw.ecommerce.module.cart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(name = "cart")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long cartId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    UserEntity user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CartItemEntity> items = new ArrayList<>();


    public Long getTotalItems(){
        return items.stream()
                .mapToLong(CartItemEntity::getQuantity)
                .sum();
    }

    public Double getTotalPrice(){
        return items.stream()
                .mapToDouble(item -> item.getProductVariant().getPrice() * item.getQuantity())
                .sum();
    }


}
