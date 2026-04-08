package com.hikiw.ecommerce.module.address.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.configuration.SecurityUtil;
import com.hikiw.ecommerce.module.address.dto.AddressRequest;
import com.hikiw.ecommerce.module.address.dto.AddressResponse;
import com.hikiw.ecommerce.module.address.entity.AddressEntity;
import com.hikiw.ecommerce.module.address.mapper.AddressMapper;
import com.hikiw.ecommerce.module.address.repository.AddressRepository;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import com.hikiw.ecommerce.module.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class AddressService {

    AddressRepository addressRepository;
    UserRepository userRepository;
    AddressMapper addressMapper;
    SecurityUtil securityUtil;


    public List<AddressResponse> getMyAddresses() {
        Long userId = securityUtil.getCurrentUserId();
        return addressRepository.findByUser_IdOrderByIsDefaultDesc(userId).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Transactional
    public AddressResponse createAddress(AddressRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        AddressEntity newAddress = addressMapper.toEntity(request);
        newAddress.setUser(user);

        if (!addressRepository.existsByUser_Id(userId)) {
            newAddress.setIsDefault(true);
        } else if (Boolean.TRUE.equals(request.getIsDefault())) {
            // Nếu đánh dấu là Default -> Gỡ Default của cái cũ
            clearOldDefaultAddress(userId);
        } else {
            newAddress.setIsDefault(false);
        }

        return addressMapper.toResponse(addressRepository.save(newAddress));
    }


    @Transactional
    public AddressResponse updateAddress(Long addressId, AddressRequest request) {
        Long userId = securityUtil.getCurrentUserId();
        AddressEntity address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        addressMapper.updateEntityFromRequest(request, address);

        // Nếu update thành Default -> Gỡ Default của cái cũ
        if (Boolean.TRUE.equals(request.getIsDefault()) && !Boolean.TRUE.equals(address.getIsDefault())) {
            clearOldDefaultAddress(userId);
            address.setIsDefault(true);
        }

        return addressMapper.toResponse(addressRepository.save(address));
    }


    @Transactional
    public AddressResponse setDefaultAddress(Long addressId) {
        Long userId = securityUtil.getCurrentUserId();
        AddressEntity address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        clearOldDefaultAddress(userId);
        address.setIsDefault(true);

        return addressMapper.toResponse(addressRepository.save(address));
    }


    @Transactional
    public void deleteAddress(Long addressId) {
        Long userId = securityUtil.getCurrentUserId();
        AddressEntity address = addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        if(Boolean.TRUE.equals(address.getIsDefault())) {
            throw new AppException(ErrorCode.CANNOT_DELETE_DEFAULT_ADDRESS);
        }
        addressRepository.delete(address);
    }

    // ==========================================
    // HÀM HELPER: Gỡ bỏ cờ Default của địa chỉ cũ
    // ==========================================
    private void clearOldDefaultAddress(Long userId) {
        addressRepository.findByUser_IdAndIsDefaultTrue(userId)
                .ifPresent(oldDefault -> {
                    oldDefault.setIsDefault(false);
                    addressRepository.save(oldDefault);
                });
    }
}