package com.hikiw.ecommerce.module.shop.repository;

import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    Optional<Object> findByShopName(String name);
    Optional<ShopEntity> findByOwner_Id(Long ownerId);
}
