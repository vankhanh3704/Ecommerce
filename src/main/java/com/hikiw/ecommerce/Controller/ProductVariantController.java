package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.product.variant.ProductVariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ProductVariantResponse;
import com.hikiw.ecommerce.Service.ProductVariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
