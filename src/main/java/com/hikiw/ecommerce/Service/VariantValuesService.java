package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.VariantEntity;
import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.VariantValuesMapper;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.Model.Response.VariantValuesDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantValuesResponse;
import com.hikiw.ecommerce.Repository.VariantRepository;
import com.hikiw.ecommerce.Repository.VariantValuesRepository;
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
