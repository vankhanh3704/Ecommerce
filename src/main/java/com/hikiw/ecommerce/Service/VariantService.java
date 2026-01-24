package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.VariantMapper;
import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.VariantUpdateRequest;
import com.hikiw.ecommerce.Model.Response.VariantDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import com.hikiw.ecommerce.Repository.VariantRepository;
import com.hikiw.ecommerce.Repository.VariantValuesRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class VariantService {
    VariantRepository variantRepository;
    VariantMapper variantMapper;
    VariantValuesRepository variantValuesRepository;

    @Transactional
    public VariantResponse createVariant(VariantCreationRequest request){
        if(variantRepository.existsByVariantName(request.getVariantName())){
            throw new AppException(ErrorCode.VARIANT_EXISTED);
        }
        var variantEntity = variantMapper.toVariantEntity(request);
        var savedEntity = variantRepository.save(variantEntity);
        return variantMapper.toVariantResponse(savedEntity);
    }

    @Transactional
    public VariantResponse updateVariant(Long variantId, VariantUpdateRequest request) {
        var variantEntity = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        variantMapper.updateVariant(variantEntity, request);
        var updatedEntity = variantRepository.save(variantEntity);
        return variantMapper.toVariantResponse(updatedEntity);
    }

    @Transactional(readOnly = true)
    public VariantDetailResponse getVariantDetailById(Long variantId) {
        var variantEntity = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        return variantMapper.toVariantDetailResponse(variantEntity);
    }

    @Transactional(readOnly = true)
    public List<VariantResponse> getAllVariants() {
        var variantEntities = variantRepository.findAll();
        return variantEntities.stream()
                .map(variantMapper::toVariantResponse)
                .toList();
    }

    @Transactional
    public void deleteVariant(Long variantId) {
        var variantEntity = variantRepository.findById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_EXISTED));
        if(variantRepository.countVariantValues(variantId) > 0){
            throw new AppException(ErrorCode.VARIANT_HAS_VALUES);
        }
        variantRepository.delete(variantEntity);
    }
}
