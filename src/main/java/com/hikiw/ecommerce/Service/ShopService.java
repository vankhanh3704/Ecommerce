package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.ShopMapper;
import com.hikiw.ecommerce.Model.Request.ShopCreateRequest;
import com.hikiw.ecommerce.Model.Response.ShopResponse;
import com.hikiw.ecommerce.Repository.ShopRepository;
import com.hikiw.ecommerce.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

public class ShopService {

    ShopRepository shopRepository;
    UserRepository userRepository;
    ShopMapper shopMapper;


    ShopResponse getShopById(Long id){
        return shopRepository.findById(id)
                .map(shopMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SHOP_NOT_EXISTED));
    }
    public ShopResponse createShop(ShopCreateRequest request){

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

}
