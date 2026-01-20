package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.ShopCreateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.ShopResponse;
import com.hikiw.ecommerce.Service.ShopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/shops")
public class ShopController {
    ShopService shopService;


    @PostMapping
    ApiResponse<ShopResponse> createShop(@RequestBody ShopCreateRequest request){
        return ApiResponse.<ShopResponse>builder()
                .result(shopService.createShop(request))
                .build();
    }
}
