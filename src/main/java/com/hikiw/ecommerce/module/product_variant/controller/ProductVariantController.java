package com.hikiw.ecommerce.module.product_variant.controller;


import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantCreationRequest;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantUpdateRequest;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantDetailResponse;
import com.hikiw.ecommerce.module.product_variant.dto.ProductVariantResponse;
import com.hikiw.ecommerce.module.product_variant.service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/product-variant")
public class ProductVariantController {

    ProductVariantService productVariantService;

    @PostMapping
    public ApiResponse<ProductVariantResponse> createProductVariant(@RequestBody ProductVariantCreationRequest request){
        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.createProductVariant(request))
                .message("Product variant created successfully")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductVariantDetailResponse> getProductVariant(@PathVariable Long id){
        return ApiResponse.<ProductVariantDetailResponse>builder()
                .result(productVariantService.getProductVariantById(id))
                .message("Product variant retrieved successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProductVariant(@PathVariable Long id) {
        productVariantService.deleteProductVariantById(id);
        return ApiResponse.<Void>builder()
                .message("Product variant deleted successfully")
                .build();
    }
    @GetMapping
    public ApiResponse<List<ProductVariantResponse>> getAllProductVariants() {
        return ApiResponse.<List<ProductVariantResponse>>builder()
                .result(productVariantService.getAllProductVariants())
                .message("Product variants retrieved successfully")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductVariantResponse> updateProductVariant(@PathVariable Long id, @RequestBody ProductVariantUpdateRequest request) {
        return ApiResponse.<ProductVariantResponse>builder()
                .result(productVariantService.updateProductVariant(id, request))
                .message("Product variant updated successfully")
                .build();
    }



}
