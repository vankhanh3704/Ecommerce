package com.hikiw.ecommerce.module.attribute.service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyUpdateRequest;
import com.hikiw.ecommerce.module.attribute.repository.ProductAttributeValueRepository;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.module.attribute.dto.AttributeKeyResponse;
import com.hikiw.ecommerce.module.attribute.entity.AttributeKeyEntity;
import com.hikiw.ecommerce.module.attribute.mapper.AttributeKeyMapper;
import com.hikiw.ecommerce.module.attribute.repository.AttributeKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AttributeKeyService {
    AttributeKeyRepository attributeKeyRepository;
    AttributeKeyMapper attributeKeyMapper;
    ProductAttributeValueRepository productAttributeValueRepository;


    @Transactional
    public AttributeKeyResponse createAttributeKey(AttributeKeyCreationRequest request){
        // Check if key name already exists
        if(attributeKeyRepository.existsByKeyName(request.getKeyName())){
            throw new AppException(ErrorCode.ATTRIBUTE_KEY_EXISTED);
        }
        AttributeKeyEntity entity = attributeKeyMapper.toEntity(request);
        return attributeKeyMapper.toResponse(attributeKeyRepository.save(entity));
    }

    @Transactional
    public AttributeKeyResponse updateAttributeKey(Long id, AttributeKeyUpdateRequest request){
        AttributeKeyEntity entity = attributeKeyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_KEY_NOT_EXISTED));

        attributeKeyMapper.updateAttributeKey(entity, request);
        attributeKeyRepository.save(entity);
        return attributeKeyMapper.toResponse(entity);
    }

    @Transactional
    public void deleteAttributeKey(Long id){
        AttributeKeyEntity entity = attributeKeyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_KEY_NOT_EXISTED));
        // Check if any product attribute values are associated with this key
        if(productAttributeValueRepository.existsByAttributeKey(entity)) {
            throw new AppException(ErrorCode.ATTRIBUTE_KEY_IN_USE_CANNOT_DELETE);
        }
        attributeKeyRepository.delete(entity);

    }



    @Transactional(readOnly = true)
    public AttributeKeyResponse getAttributeKeyById(Long id){
        AttributeKeyEntity entity = attributeKeyRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_KEY_NOT_EXISTED));

        return attributeKeyMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<AttributeKeyResponse> getAllAttributeKeys(){
        List<AttributeKeyEntity> entities = attributeKeyRepository.findAll();
        return entities.stream()
                .map(attributeKeyMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AttributeKeyResponse> getAllAttributeKeysActive() {
        return attributeKeyRepository
                .findAllActiveOrderedByDisplayOrder()
                .stream()
                .map(attributeKeyMapper::toResponse)
                .toList();
    }


}
