package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.ProductAttributeValueBatchRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.ProductAttributeValueCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.ProductAttributeValueUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.AttributeKeyResponse;
import com.hikiw.ecommerce.Model.Response.ProductAttributeValueResponse;
import com.hikiw.ecommerce.Service.ProductAttributeValueService;
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
