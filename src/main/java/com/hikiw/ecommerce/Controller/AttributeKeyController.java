package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyCreationRequest;
import com.hikiw.ecommerce.Model.Request.product.attribute.AttributeKeyUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.AttributeKeyResponse;
import com.hikiw.ecommerce.Service.AttributeKeyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/attribute-key")
public class AttributeKeyController {
    AttributeKeyService attributeKeyService;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<AttributeKeyResponse> createAttributeKey(@RequestBody AttributeKeyCreationRequest attributeKeyCreationRequest) {
        return ApiResponse.<AttributeKeyResponse>builder()
                .result(attributeKeyService.createAttributeKey(attributeKeyCreationRequest))
                .message("Attribute key created successfully")
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<AttributeKeyResponse> updateAttributeKey(@PathVariable Long id, @RequestBody AttributeKeyUpdateRequest request) {
        return ApiResponse.<AttributeKeyResponse>builder()
                .result(attributeKeyService.updateAttributeKey(id, request))
                .message("Attribute key updated successfully")
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteAttributeKey(@PathVariable Long id) {
        attributeKeyService.deleteAttributeKey(id);
        return ApiResponse.<Void>builder()
                .message("Attribute key deleted successfully")
                .build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<AttributeKeyResponse>> getAllAttributeKeys() {
        return ApiResponse.<List<AttributeKeyResponse>>builder()
                .result(attributeKeyService.getAllAttributeKeys())
                .message("Attribute keys retrieved successfully")
                .build();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ApiResponse<List<AttributeKeyResponse>> getActiveAttributeKeys() {
        return ApiResponse.<List<AttributeKeyResponse>>builder()
                .result(attributeKeyService.getAllAttributeKeysActive())
                .message("Active attribute keys retrieved successfully")
                .build();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<AttributeKeyResponse> getAttributeKeyById(@PathVariable Long id) {
        return ApiResponse.<AttributeKeyResponse>builder()
                .result(attributeKeyService.getAttributeKeyById(id))
                .message("Attribute key retrieved successfully")
                .build();
    }
}
