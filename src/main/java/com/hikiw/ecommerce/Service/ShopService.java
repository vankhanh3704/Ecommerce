package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.ShopEntity;
import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.ShopLocationMapper;
import com.hikiw.ecommerce.Mapper.ShopMapper;
import com.hikiw.ecommerce.Model.Request.shop.ShopCreationRequest;
import com.hikiw.ecommerce.Model.Request.shop.ShopUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ShopDetailResponse;
import com.hikiw.ecommerce.Model.Response.ShopResponse;
import com.hikiw.ecommerce.Repository.ShopLocationRepository;
import com.hikiw.ecommerce.Repository.ShopRepository;
import com.hikiw.ecommerce.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ShopService {
    private final ShopLocationMapper shopLocationMapper;

    ShopRepository shopRepository;
    ShopLocationRepository locationRepository;
    UserRepository userRepository;
    ShopMapper shopMapper;
    private final ShopLocationRepository shopLocationRepository;


    public ShopResponse getShopById(Long id){
        return shopRepository.findById(id)
                .map(shopMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));
    }
    @Transactional
    public ShopResponse createShop(ShopCreationRequest request){
        UserEntity user = userRepository.findById(request.getOwnerUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(shopRepository.findByShopName((request.getShopName())).isPresent()){
            throw new AppException(ErrorCode.SHOP_EXISTED);
        }
        var shopEntity = shopMapper.toEntity(request);
        shopEntity.setOwner(user);
        var savedEntity = shopRepository.save(shopEntity);
        return shopMapper.toResponse(savedEntity);
    }

    @Transactional
    public void deleteShop(Long id){
        ShopEntity shop = shopRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));
        shop.setIsActive(false);
        shopRepository.save(shop);
    }

    @Transactional
    public List<ShopResponse> getAllShops(){
        return shopRepository.findAll()
                .stream()
                .map(shopMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShopResponse updateShop(Long id, ShopUpdateRequest request){
        ShopEntity shopEntity = shopRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));

        shopMapper.toUpdateShop(shopEntity, request);
        ShopEntity updatedEntity = shopRepository.save(shopEntity);
        return shopMapper.toResponse(updatedEntity);
    }

    @Transactional
    public ShopDetailResponse getShopByOwnerId(Long ownerId){
        ShopEntity shop = shopRepository.findByOwner_Id(ownerId).orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));
        return shopMapper.toDetailResponse(shop);
    }
}
