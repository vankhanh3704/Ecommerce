package com.hikiw.ecommerce.module.cart.repository;

import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.entity.CartItemEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUser_Id(Long userId);
}

