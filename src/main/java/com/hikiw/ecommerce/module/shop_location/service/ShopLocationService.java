package com.hikiw.ecommerce.module.shop_location.service;

import com.hikiw.ecommerce.module.shop.entity.ShopEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.shop.repository.ShopRepository;
import com.hikiw.ecommerce.module.shop_location.dto.ShopLocationUpdateRequest;
import com.hikiw.ecommerce.module.shop_location.dto.ShopLocationCreationRequest;
import com.hikiw.ecommerce.module.shop_location.dto.ShopLocationResponse;
import com.hikiw.ecommerce.module.shop_location.entity.ShopLocationEntity;
import com.hikiw.ecommerce.module.shop_location.mapper.ShopLocationMapper;
import com.hikiw.ecommerce.module.shop_location.repository.ShopLocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ShopLocationService {
    //create , read, update, delete shop locations

    ShopLocationRepository shopLocationRepository;
    ShopRepository shopRepository;
    ShopLocationMapper shopLocationMapper;


    @Transactional
    public ShopLocationResponse createShopLocation(ShopLocationCreationRequest request) {

        // Kiểm tra đã tồn tại location tương tự cho shop chưa
        // Nếu có thì ném lỗi
        ShopEntity shop = shopRepository.findById(request.getShopId())
                .orElseThrow( () -> new AppException(ErrorCode.SHOP_NOT_EXISTED));

        if(Boolean.TRUE.equals(request.getIsDefaultPickup())){
            shopLocationRepository.findByShop_ShopIdAndIsDefaultPickupTrue(request.getShopId())
                    .ifPresent( existingLocation -> {
                        throw new AppException(ErrorCode.DEFAULT_PICKUP_LOCATION_ALREADY_EXISTS);
                    });
        }
        if(Boolean.TRUE.equals(request.getIsDefaultReturn())){
            // Tương tự cho isDefaultReturn nếu cần
            shopLocationRepository.findByShop_ShopIdAndIsDefaultReturnTrue(request.getShopId())
                    .ifPresent( existingLocation -> {
                        throw new AppException(ErrorCode.DEFAULT_PICKUP_LOCATION_ALREADY_EXISTS);
                    });
        }
        var shopLocationEntity = shopLocationMapper.toEntity(request);
        shopLocationEntity.setShop(shop);
        var savedEntity = shopLocationRepository.save(shopLocationEntity);

        return shopLocationMapper.toResponse(savedEntity);
    }

    @Transactional
    public ShopLocationResponse getShopLocationById(Long id){
        return shopLocationRepository.findById(id)
                .map(shopLocationMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_LOCATION_NOT_EXISTED));
    }

    @Transactional
    public ShopLocationResponse updateShopLocation(Long id, ShopLocationUpdateRequest request){
        ShopLocationEntity shopLocation = shopLocationRepository.findById(id).orElseThrow( () -> new AppException(ErrorCode.SHOP_LOCATION_NOT_EXISTED));

        shopLocationMapper.updateShopLocationFromRequest(shopLocation, request);
        ShopLocationEntity updatedEntity = shopLocationRepository.save(shopLocation);
        return shopLocationMapper.toResponse(updatedEntity);

    }

    @Transactional
    public void deleteShopLocationById(Long id){
        ShopLocationEntity shopLocation = shopLocationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_LOCATION_NOT_EXISTED));
        shopLocation.setIsActive(false);
        shopLocationRepository.save(shopLocation);
    }

    @Transactional
    public List<ShopLocationResponse> getAllShopLocations(){
        return shopLocationRepository.findAll()
                .stream()
                .map(shopLocationMapper::toResponse)
                .toList();
    }


}
