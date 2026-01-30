package com.hikiw.ecommerce.module.shop.controller;


import com.hikiw.ecommerce.module.shop.dto.ShopCreationRequest;
import com.hikiw.ecommerce.module.shop.dto.ShopUpdateRequest;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.shop.dto.ShopDetailResponse;
import com.hikiw.ecommerce.module.shop.dto.ShopResponse;
import com.hikiw.ecommerce.module.shop.service.ShopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/shops")
public class ShopController {
    ShopService shopService;


    @PostMapping
    ApiResponse<ShopResponse> createShop(@RequestBody ShopCreationRequest request){
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.createShop(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ShopResponse> getShopById(@PathVariable Long id){
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.getShopById(id))
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    ApiResponse<String> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return ApiResponse.<String>builder()
                .result("Shop deleted successfully")
                .build();
    }

    @RequestMapping(method = RequestMethod.GET)
    ApiResponse<List<ShopResponse>> getAllShops(){
        return ApiResponse.<List<ShopResponse>>builder()
                .result(shopService.getAllShops())
                .build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    ApiResponse<ShopResponse> updateShop(@PathVariable Long id, @RequestBody ShopUpdateRequest request){
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.updateShop(id, request))
                .build();
    }

    @RequestMapping(value = "/owner/{ownerId}", method = RequestMethod.GET)
    ApiResponse<ShopDetailResponse> getShopsByOwnerId(@PathVariable Long ownerId) {
        return ApiResponse.<ShopDetailResponse>builder()
                .result(shopService.getShopByOwnerId(ownerId))
                .build();
    }
}
