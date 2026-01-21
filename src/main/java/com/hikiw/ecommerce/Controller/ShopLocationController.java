package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.ShopLocationCreationRequest;
import com.hikiw.ecommerce.Model.Request.ShopLocationUpdateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ShopLocationResponse;
import com.hikiw.ecommerce.Service.ShopLocationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop-locations")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShopLocationController {

    ShopLocationService shopLocationService;

    @PostMapping
    public ApiResponse<ShopLocationResponse> createShopLocation(@RequestBody ShopLocationCreationRequest request) {
        return ApiResponse.<ShopLocationResponse>builder()
                .result(shopLocationService.createShopLocation(request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponse<ShopLocationResponse> getShopLocationById(@PathVariable("id") Long id) {
        return ApiResponse.<ShopLocationResponse>builder()
                .result(shopLocationService.getShopLocationById(id))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResponse<ShopLocationResponse> updateShopLocation(@PathVariable Long id, @RequestBody ShopLocationUpdateRequest request){
        return ApiResponse.<ShopLocationResponse>builder()
                .result(shopLocationService.updateShopLocation(id, request))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteShopLocation(@PathVariable Long id){
        shopLocationService.deleteShopLocationById(id);
        return ApiResponse.<Void>builder().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResponse<List<ShopLocationResponse>> getAllShopLocations(){
        return ApiResponse.<List<ShopLocationResponse>>builder()
                .result(shopLocationService.getAllShopLocations())
                .build();
    }
}

