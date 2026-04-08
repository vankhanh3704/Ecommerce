package com.hikiw.ecommerce.module.address.controller;

import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.address.dto.AddressRequest;
import com.hikiw.ecommerce.module.address.dto.AddressResponse;
import com.hikiw.ecommerce.module.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {

    AddressService addressService;

    @GetMapping
    public ApiResponse<List<AddressResponse>> getMyAddresses() {
        return ApiResponse.<List<AddressResponse>>builder()
                .result(addressService.getMyAddresses())
                .build();
    }

    @PostMapping
    public ApiResponse<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.createAddress(request))
                .message("Tạo địa chỉ thành công")
                .build();
    }

    @PutMapping("/{addressId}")
    public ApiResponse<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.updateAddress(addressId, request))
                .message("Cập nhật địa chỉ thành công")
                .build();
    }

    @PutMapping("/{addressId}/default")
    public ApiResponse<AddressResponse> setDefaultAddress(@PathVariable Long addressId) {
        return ApiResponse.<AddressResponse>builder()
                .result(addressService.setDefaultAddress(addressId))
                .message("Đã thiết lập làm địa chỉ mặc định")
                .build();
    }

    @DeleteMapping("/{addressId}")
    public ApiResponse<String> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ApiResponse.<String>builder()
                .result("Đã xóa địa chỉ")
                .build();
    }
}