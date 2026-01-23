package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.variant.VariantCreationRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.VariantResponse;
import com.hikiw.ecommerce.Service.VariantService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
