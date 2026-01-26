package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingBatchRequest;
import com.hikiw.ecommerce.Model.Request.product.variant.mapping.ProductVariantMappingCreationRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ProductVariantMappingResponse;
import com.hikiw.ecommerce.Service.ProductVariantMappingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product-variant-mapping")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductVariantMappingController {
    ProductVariantMappingService productVariantMappingService;

    @PostMapping
    public ApiResponse<ProductVariantMappingResponse> createMapping(@RequestBody ProductVariantMappingCreationRequest request){
        return ApiResponse.<ProductVariantMappingResponse>builder()
                .result(productVariantMappingService.createMapping(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductVariantMappingResponse> getMapping(@PathVariable Long id){
        return ApiResponse.<ProductVariantMappingResponse>builder()
                .result(productVariantMappingService.getMappingById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMapping(@PathVariable Long id){
        productVariantMappingService.deleteMappingById(id);
        return ApiResponse.<Void>builder()
                .message("Successfully deleted")
                .build();
    }

    @PostMapping("/batch")
    public ApiResponse<List<ProductVariantMappingResponse>> createMappingsBatch(@RequestBody ProductVariantMappingBatchRequest requests){
        return ApiResponse.<List<ProductVariantMappingResponse>>builder()
                .result(productVariantMappingService.createMappingsBatch(requests))
                .message("Successfully created product mapping batch")
                .build();
    }

    @GetMapping("/product-variant/{productVariantId}")
    public ApiResponse<List<ProductVariantMappingResponse>> getMappingsByProductVariantId(@PathVariable Long productVariantId){
        return ApiResponse.<List<ProductVariantMappingResponse>>builder()
                .result(productVariantMappingService.getMappingsByProductVariantId(productVariantId))
                .build();
    }
}
