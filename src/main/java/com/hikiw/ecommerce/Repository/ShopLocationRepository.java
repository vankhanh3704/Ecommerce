package com.hikiw.ecommerce.Repository;

import com.hikiw.ecommerce.Entity.ShopLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ShopLocationRepository extends JpaRepository<ShopLocationEntity, Long> {
    Optional<ShopLocationEntity> findByShop_ShopIdAndIsDefaultPickupTrue (Long shopId);
    Optional<ShopLocationEntity> findByShop_ShopIdAndIsDefaultReturnTrue (Long shopId);
    List<ShopLocationEntity> findByShop_ShopId(Long shopId);
}
