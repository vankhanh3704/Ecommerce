package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Request.variant.VariantUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.VariantDetailResponse;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import com.hikiw.ecommerce.Service.VariantService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
