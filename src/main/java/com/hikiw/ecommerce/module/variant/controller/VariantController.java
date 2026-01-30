package com.hikiw.ecommerce.module.variant.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.variant.dto.VariantCreationRequest;
import com.hikiw.ecommerce.module.variant.dto.VariantDetailResponse;
import com.hikiw.ecommerce.module.variant.dto.VariantResponse;
import com.hikiw.ecommerce.module.variant.dto.VariantUpdateRequest;
import com.hikiw.ecommerce.module.variant.service.VariantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/variants")
public class VariantController {
    VariantService variantService;

    @RequestMapping(method = RequestMethod.POST)
    ApiResponse<VariantResponse> createVariant(@RequestBody VariantCreationRequest request){
        return ApiResponse.<VariantResponse>builder()
                .result(variantService.createVariant(request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    ApiResponse<VariantDetailResponse> getVariant(@PathVariable Long id){
        return ApiResponse.<VariantDetailResponse>builder()
                .result(variantService.getVariantDetailById(id))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ApiResponse<VariantResponse> updateVariant(@PathVariable Long id, @RequestBody VariantUpdateRequest request) {
        return ApiResponse.<VariantResponse>builder()
                .result(variantService.updateVariant(id, request))
                .build();
    }

    @RequestMapping(method = RequestMethod.GET)
    ApiResponse<List<VariantResponse>> getAllVariants() {
        return ApiResponse.<List<VariantResponse>>builder()
                .result(variantService.getAllVariants())
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ApiResponse<String> deleteVariant(@PathVariable Long id) {
        variantService.deleteVariant(id);
        return ApiResponse.<String>builder()
                .result("Variant deleted successfully")
                .build();
    }
}
