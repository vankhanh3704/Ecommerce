package com.hikiw.ecommerce.module.attribute.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueCreationRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueResponse;
import com.hikiw.ecommerce.module.attribute.service.ProductAttributeValueService;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueUpdateRequest;
import com.hikiw.ecommerce.module.attribute.dto.ProductAttributeValueBatchRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-attribute-value")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductAttributeValueController {
    ProductAttributeValueService productAttributeValueService;


    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<ProductAttributeValueResponse> addAttributeToProduct(@RequestBody ProductAttributeValueCreationRequest request){
        return ApiResponse.<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.addAttributeToProduct(request))
                .message("Attribute added to product successfully")
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<ProductAttributeValueResponse> updateAttributeValue(@PathVariable("id") Long id, @RequestBody ProductAttributeValueUpdateRequest request){
        return ApiResponse.<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.updateAttributeValue(id, request))
                .message("Attribute updated successfully")
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<ProductAttributeValueResponse> getAttributeValue(@PathVariable("id") Long id){
        return ApiResponse.<ProductAttributeValueResponse>builder()
                .result(productAttributeValueService.getAttributeValue(id))
                .build();
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public ApiResponse<List<ProductAttributeValueResponse>> getAttributeValuesByProduct(@PathVariable("id") Long id){
        return ApiResponse.<List<ProductAttributeValueResponse>>builder()
                .result(productAttributeValueService.getAttributeValuesByProduct(id))
                .build();
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ApiResponse<Void> delete(@PathVariable("id") Long id){
        productAttributeValueService.deleteProductAttribute(id);
        return ApiResponse.<Void>builder()
                 .message("Product attribute deleted successfully")
                 .build();
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteProductAttribute(@PathVariable("id") Long id){
        productAttributeValueService.deleteProductAttributeValueByProductId(id);
        return ApiResponse.<Void>builder()
                .message("Product attribute deleted successfully")
                .build();
    }

    @RequestMapping(value = "/batch", method = RequestMethod.POST)
    public ApiResponse<List<ProductAttributeValueResponse>> addAttributesBatch(@RequestBody ProductAttributeValueBatchRequest requests){
        return ApiResponse.<List<ProductAttributeValueResponse>>builder()
                .result(productAttributeValueService.addAttributesBatch(requests))
                .build();
    }
}
