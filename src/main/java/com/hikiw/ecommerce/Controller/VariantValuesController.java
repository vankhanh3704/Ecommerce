package com.hikiw.ecommerce.Controller;

import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.values.VariantValuesUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.VariantValuesDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantValuesResponse;
import com.hikiw.ecommerce.Service.VariantValuesService;
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
