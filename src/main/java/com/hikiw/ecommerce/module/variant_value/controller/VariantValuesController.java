package com.hikiw.ecommerce.module.variant_value.controller;

import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesCreationRequest;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesDetailResponse;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesResponse;
import com.hikiw.ecommerce.module.variant_value.dto.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.module.variant_value.service.VariantValuesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variant-values")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VariantValuesController {
    VariantValuesService variantValuesService;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResponse<VariantValuesResponse> createVariantValue(@RequestBody VariantValuesCreationRequest request){
        return ApiResponse.<VariantValuesResponse>builder()
                .result(variantValuesService.createVariantValue(request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<VariantValuesResponse> updateVariantValue(@PathVariable Long id, @RequestBody VariantValuesUpdateRequest request) {
        return ApiResponse.<VariantValuesResponse>builder()
                .result(variantValuesService.updateVariantValue(id,request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<VariantValuesDetailResponse> getVariantValueById(@PathVariable Long id) {
        return ApiResponse.<VariantValuesDetailResponse>builder()
                .result(variantValuesService.getVariantValueDetailById(id))
                .build();
    }

    @RequestMapping(value = "/variant/{variantId}", method = RequestMethod.GET)
    public ApiResponse<List<VariantValuesResponse>> getVariantValuesByVariantId(@PathVariable Long variantId){
        return ApiResponse.<List<VariantValuesResponse>>builder()
                .result(variantValuesService.getVariantValuesByVariantId(variantId))
                .build();
    }

}
