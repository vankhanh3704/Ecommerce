package com.hikiw.ecommerce.module.attribute.service;


import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueUpdateRequest;
import com.hikiw.ecommerce.module.attribute.dto.AttributeValueRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueBatchRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueCreationRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueResponse;
import com.hikiw.ecommerce.module.attribute.entity.AttributeKeyEntity;
import com.hikiw.ecommerce.module.attribute.entity.ProductAttributeValueEntity;
import com.hikiw.ecommerce.module.attribute.repository.AttributeKeyRepository;
import com.hikiw.ecommerce.module.attribute.repository.ProductAttributeValueRepository;
import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.attribute.mapper.ProductAttributeValueMapper;
import com.hikiw.ecommerce.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductAttributeValueService {

    ProductAttributeValueRepository productAttributeValueRepository;
    ProductAttributeValueMapper productAttributeValueMapper;
    AttributeKeyRepository attributeKeyRepository;
    ProductRepository productRepository;
    @Transactional
    public ProductAttributeValueResponse addAttributeToProduct(ProductAttributeValueCreationRequest request){
        ProductEntity product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));
        AttributeKeyEntity attributeKey = attributeKeyRepository
                .findById(request.getAttributeKeyId())
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_KEY_NOT_EXISTED));

        var entity = productAttributeValueMapper.toEntity(request);
        entity.setProduct(product);
        entity.setAttributeKey(attributeKey);
        var savedEntity = productAttributeValueRepository.save(entity);
        return productAttributeValueMapper.toResponse(savedEntity);
    }

    @Transactional
    public ProductAttributeValueResponse updateAttributeValue(Long id, ProductAttributeValueUpdateRequest request){
        ProductAttributeValueEntity entity = productAttributeValueRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUES_NOT_EXISTED));

        productAttributeValueMapper.toUpdate(entity, request);
        productAttributeValueRepository.save(entity);
        return productAttributeValueMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public ProductAttributeValueResponse getAttributeValue(Long id){

        ProductAttributeValueEntity entity = productAttributeValueRepository
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUES_NOT_EXISTED));

        return productAttributeValueMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<ProductAttributeValueResponse> getAttributeValuesByProduct(Long productId){
        ProductEntity product = productRepository
                .findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ProductAttributeValueEntity> attributeValues = productAttributeValueRepository.findByProduct_ProductId(productId);

        return productAttributeValueMapper.toResponseList(attributeValues);
    }

    @Transactional
    public void deleteProductAttribute(Long pavId) {
        ProductAttributeValueEntity attributeValue = productAttributeValueRepository.findById(pavId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_ATTRIBUTE_VALUES_NOT_EXISTED));
        productAttributeValueRepository.delete(attributeValue);
    }

    @Transactional
    public void deleteProductAttributeValueByProductId(Long productId) {
        productAttributeValueRepository.deleteByProduct_ProductId(productId);
    }


    @Transactional
    public List<ProductAttributeValueResponse> addAttributesBatch(ProductAttributeValueBatchRequest request){
        ProductEntity product = productRepository.findById(request.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ProductAttributeValueEntity> savedValues = new ArrayList<>();

        for(AttributeValueRequest value : request.getAttributes()){
            AttributeKeyEntity attributeKey = attributeKeyRepository
                    .findById(value.getAttributeKeyId())
                    .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_KEY_NOT_EXISTED));

            if(productAttributeValueRepository.existsByProduct_ProductIdAndAttributeKey_AttributeKeyId(request.getProductId(), value.getAttributeKeyId())){
                continue;
            }
            ProductAttributeValueEntity attributeValue = ProductAttributeValueEntity.builder()
                    .product(product)
                    .attributeKey(attributeKey)
                    .valueText(value.getValueText())
                    .build();
            savedValues.add(productAttributeValueRepository.save(attributeValue));

        }
        return productAttributeValueMapper.toResponseList(savedValues);
    }
}
