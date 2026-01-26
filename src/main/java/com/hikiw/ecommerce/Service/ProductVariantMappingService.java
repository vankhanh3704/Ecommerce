package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Entity.ProductVariantEntity;
import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.ProductVariantMappingMapper;
import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingBatchRequest;
import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.Model.Response.ProductVariantMappingResponse;
import com.hikiw.ecommerce.Repository.ProductVariantMappingRepository;
import com.hikiw.ecommerce.Repository.ProductVariantRepository;
import com.hikiw.ecommerce.Repository.VariantValuesRepository;
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
