package com.hikiw.ecommerce.module.variant.controller;

import com.hikiw.ecommerce.module.product.dto.ProductCreationRequest;
import com.hikiw.ecommerce.module.product.dto.ProductUpdateRequest;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.product.dto.ProductResponse;
import com.hikiw.ecommerce.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @RequestMapping(method = RequestMethod.POST)
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreationRequest request){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ApiResponse<ProductResponse> getProductById(@PathVariable Long id){
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(id))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ApiResponse<String> deleteProduct(@PathVariable Long id){
        productService.deleteProductById(id);
        return ApiResponse.<String>builder()
                .result("success")
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ApiResponse<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }
    @RequestMapping(method = RequestMethod.GET)
    ApiResponse<List<ProductResponse>> getAllProducts(){
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productService.getAllProducts())
                .build();
    }

    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    ApiResponse<String> activateProduct(@PathVariable Long id){
        productService.activateProduct(id);
        return ApiResponse.<String>builder()
                .result("Product activated successfully")
                .build();
    }
}
