package com.hikiw.ecommerce.module.product_variant.service;


import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantEntity;
import com.hikiw.ecommerce.module.product_variant.entity.ProductVariantMappingEntity;
import com.hikiw.ecommerce.module.variant_value.entity.VariantValuesEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.product_variant.mapper.ProductVariantMappingMapper;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantMappingBatchRequest;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantMappingResponse;
import com.hikiw.ecommerce.module.product_variant.repository.ProductVariantMappingRepository;
import com.hikiw.ecommerce.module.product_variant.repository.ProductVariantRepository;
import com.hikiw.ecommerce.module.variant_value.repository.VariantValuesRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductVariantMappingService {
    ProductVariantMappingRepository productVariantMappingRepository;
    ProductVariantMappingMapper productVariantMappingMapper;
    ProductVariantRepository productVariantRepository;
    VariantValuesRepository variantValuesRepository;



    @Transactional
    public ProductVariantMappingResponse createMapping(@org.jetbrains.annotations.NotNull ProductVariantMappingCreationRequest request) {
        ProductVariantEntity productVariant = productVariantRepository
                .findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        VariantValuesEntity variantValue = variantValuesRepository
                .findById(request.getVariantValueId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_EXISTED));

        if(productVariantMappingRepository.existsByProductVariant_ProductVariantIdAndVariantValue_VariantValueId( productVariant.getProductVariantId(),
                variantValue.getVariantValueId())) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_MAPPING_EXISTED);
        }
        var mappingEntity = productVariantMappingMapper.toEntity(request);
        mappingEntity.setProductVariant(productVariant);
        mappingEntity.setVariantValue(variantValue);
        var savedMapping = productVariantMappingRepository.save(mappingEntity);
        return productVariantMappingMapper.toResponse(savedMapping);
    }

    @Transactional(readOnly = true)
    public ProductVariantMappingResponse getMappingById(Long id){
        var mappingEntity = productVariantMappingRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_MAPPING_EXISTED));
        return productVariantMappingMapper.toResponse(mappingEntity);
    }

    @Transactional
    public void deleteMappingById(Long id){
        var mappingEntity = productVariantMappingRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_MAPPING_NOT_EXISTED));
        productVariantMappingRepository.delete(mappingEntity);
    }

    @Transactional
    // Batch creation of product variant mappings
    //  VD: SKU "AT-DO-M" cần 2 mappings: Màu=Đỏ, Size=M
    public List<ProductVariantMappingResponse> createMappingsBatch(ProductVariantMappingBatchRequest request) {
        ProductVariantEntity productVariant = productVariantRepository
                .findById(request.getProductVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_EXISTED));

        List<ProductVariantMappingEntity> mappings = new ArrayList<>();

        for(var variantValueId : request.getVariantValueIds()) {
            VariantValuesEntity variantValue = variantValuesRepository
                    .findById(variantValueId)
                    .orElseThrow(() -> new AppException(ErrorCode.VARIANT_VALUE_NOT_EXISTED));

            // Check for existing mapping to avoid duplicates
            if(productVariantMappingRepository.existsByProductVariant_ProductVariantIdAndVariantValue_VariantValueId( productVariant.getProductVariantId(),
                    variantValue.getVariantValueId())) {
                log.warn("Mapping already exists for variant value ID: {}, skipping", variantValueId);
                continue;
            }

            var mappingEntity = ProductVariantMappingEntity.builder()
                    .productVariant(productVariant)
                    .variantValue(variantValue)
                    .build();
            mappings.add(productVariantMappingRepository.save(mappingEntity));
        }
        return productVariantMappingMapper.toResponseList(mappings);
    }

    @Transactional
    public List<ProductVariantMappingResponse> getMappingsByProductVariantId(Long productVariantId) {
        List<ProductVariantMappingEntity> mappingEntities = productVariantMappingRepository
                .findByProductVariant_ProductVariantId(productVariantId);

        return productVariantMappingMapper.toResponseList(mappingEntities);
    }
}
