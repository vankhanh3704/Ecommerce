package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.VariantMapper;
import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import com.hikiw.ecommerce.Repository.VariantRepository;
import com.hikiw.ecommerce.Repository.VariantValuesRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
