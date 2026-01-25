package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Entity.ProductEntity;
import com.hikiw.ecommerce.Entity.ProductVariantMappingEntity;
import com.hikiw.ecommerce.Entity.VariantValuesEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.ProductVariantMapper;
import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.ProductResponse;
import com.hikiw.ecommerce.Model.Response.ProductVariantResponse;
import com.hikiw.ecommerce.Repository.ProductRepository;
import com.hikiw.ecommerce.Repository.ProductVariantMappingRepository;
import com.hikiw.ecommerce.Repository.ProductVariantRepository;
import com.hikiw.ecommerce.Repository.VariantValuesRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ProductVariantService {
    ProductVariantRepository productVariantRepository;
    ProductVariantMappingRepository productVariantMappingRepository;
    ProductVariantMapper productVariantMapper;
    ProductRepository productRepository;
    VariantValuesRepository variantValuesRepository;
    @Transactional
    public ProductVariantResponse createProductVariant(ProductVariantCreationRequest request){
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<VariantValuesEntity> variantValues = variantValuesRepository
                .findByVariantValueIdIn(request.getVariantValueIds());

        // Validate all variant values exist
        if(variantValues.size() != request.getVariantValueIds().size()){
            throw new AppException(ErrorCode.VARIANT_VALUE_NOT_EXISTED);
        }
        // Validate SKU uniqueness
        if(productVariantRepository.existsBySku(request.getSku())){
            throw new AppException(ErrorCode.PRODUCT_VARIANT_SKU_EXISTED);
        }
        var entity = productVariantMapper.toEntity(request);
        entity.setProduct(product);
        var savedEntity = productVariantRepository.save(entity);

        // Create mappings between product variant and variant values
        for(var variantValue : variantValues){
            ProductVariantMappingEntity mapping = ProductVariantMappingEntity
                    .builder()
                    .productVariant(savedEntity)
                    .variantValue(variantValue)
                    .build();
            productVariantMappingRepository.save(mapping);
            savedEntity.getVariantMappings().add(mapping); // Maintain bidirectional relationship
        }
        return productVariantMapper.toResponse(savedEntity);
    }



}
