package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Repository.ProductVariantMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantMappingService {
    ProductVariantMappingRepository productVariantMappingRepository;


//    @Transactional
//    public
}
