package com.hikiw.ecommerce.module.variant_value.service;

import com.hikiw.ecommerce.module.variant.entity.VariantEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.variant_value.mapper.VariantValuesMapper;
import com.hikiw.ecommerce.module.variant.repository.VariantRepository;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesCreationRequest;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesDetailResponse;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesResponse;
import com.hikiw.ecommerce.module.variant_value.entity.VariantValuesEntity;
import com.hikiw.ecommerce.module.variant_value.repository.VariantValuesRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class VariantValuesService {
    VariantValuesRepository variantValuesRepository;
    VariantValuesMapper variantValuesMapper;
    VariantRepository variantRepository;

    @Transactional
    public VariantValuesResponse createVariantValue(VariantValuesCreationRequest request){
        VariantEntity variant = variantRepository
                .findById(request.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        if(variantValuesRepository.existsByVariant_VariantIdAndValueName(variant.getVariantId(), request.getValueName())){
            throw new AppException(ErrorCode.VARIANT_VALUE_EXISTED);
        }
        VariantValuesEntity variantValuesEntity = variantValuesMapper.toEntity(request);
        variantValuesEntity.setVariant(variant);
        var savedVariantValues = variantValuesRepository.save(variantValuesEntity);
        return variantValuesMapper.toResponse(savedVariantValues);
    }

    @Transactional
    public VariantValuesResponse updateVariantValue(Long variantValueId, VariantValuesUpdateRequest request){
        VariantValuesEntity entity = variantValuesRepository.findById(variantValueId)
                .orElseThrow(()-> new AppException(ErrorCode.VARIANT_VALUE_NOT_EXISTED));
        variantValuesMapper.toUpdateVariantValue(entity, request);
        var updatedEntity = variantValuesRepository.save(entity);
        return variantValuesMapper.toResponse(updatedEntity);
    }

    @Transactional
    public VariantValuesDetailResponse getVariantValueDetailById(Long id){
        VariantValuesEntity entity = variantValuesRepository
                .findById(id)
                .orElseThrow( ()-> new AppException(ErrorCode.VARIANT_VALUE_NOT_EXISTED));
        return variantValuesMapper.toDetailResponse(entity);
    }

    @Transactional
    public List<VariantValuesResponse> getVariantValuesByVariantId(Long variantId){
        VariantEntity variant = variantRepository
                .findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        List<VariantValuesEntity> variantValuesEntities = variantValuesRepository.findByVariantIdOrderedByDisplayOrder(variant.getVariantId());
        return variantValuesEntities.stream()
                .map(variantValuesMapper::toResponse)
                .toList();
    }


}
