package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantService {
    ProductVariantRepository productVariantRepository;
}
