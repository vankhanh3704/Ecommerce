package com.hikiw.ecommerce.Controller;

import com.hikiw.ecommerce.Model.Request.product.ProductCreateRequest;
import com.hikiw.ecommerce.Model.Request.product.ProductUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ProductResponse;
import com.hikiw.ecommerce.Service.ProductService;
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
    ApiResponse<ProductResponse> createProduct(@RequestBody ProductCreateRequest request){
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
