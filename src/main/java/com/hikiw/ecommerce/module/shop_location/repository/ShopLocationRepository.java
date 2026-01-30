package com.hikiw.ecommerce.module.shop_location.repository;

import com.hikiw.ecommerce.module.shop_location.entity.ShopLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopLocationRepository extends JpaRepository<ShopLocationEntity, Long> {
    Optional<ShopLocationEntity> findByShop_ShopIdAndIsDefaultPickupTrue (Long shopId);
    Optional<ShopLocationEntity> findByShop_ShopIdAndIsDefaultReturnTrue (Long shopId);
    List<ShopLocationEntity> findByShop_ShopId(Long shopId);
}
