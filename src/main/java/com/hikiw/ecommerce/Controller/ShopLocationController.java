package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.ShopLocationCreationRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ShopLocationResponse;
import com.hikiw.ecommerce.Service.ShopLocationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop-locations")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShopLocationController {

    ShopLocationService shopLocationService;

    @PostMapping
    public ApiResponse<ShopLocationResponse> createShopLocation(ShopLocationCreationRequest request) {
        return ApiResponse.<ShopLocationResponse>builder()
                .result(shopLocationService.createShopLocation(request))
                .build();
    }
}

