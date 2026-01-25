package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ProductVariantMappingResponse;
import com.hikiw.ecommerce.Service.ProductVariantMappingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product-variant-mapping")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantMappingController {
    ProductVariantMappingService productVariantMappingService;

    @PostMapping
    public ApiResponse<ProductVariantMappingResponse> createMapping(ProductVariantMappingCreationRequest request){
        return ApiResponse.<ProductVariantMappingResponse>builder()
                .result(productVariantMappingService.createMapping(request))
                .build();
    }
}
