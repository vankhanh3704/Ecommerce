package com.hikiw.ecommerce.module.cart.repository;


import com.hikiw.ecommerce.module.cart.entity.CartEntity;
import com.hikiw.ecommerce.module.cart.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByCart_CartIdAndProductVariant_ProductVariantId(Long cartId, Long productVariantId);

}
