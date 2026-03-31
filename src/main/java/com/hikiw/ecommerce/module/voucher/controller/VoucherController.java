package com.hikiw.ecommerce.module.voucher.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.voucher.dto.*;
import com.hikiw.ecommerce.module.voucher.service.VoucherService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/vouchers")
public class VoucherController {
    VoucherService voucherService;


    @PostMapping
    public ApiResponse<VoucherResponse> createVoucher(@RequestBody VoucherCreationRequest request){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.createVoucher(request))
                .build();
    }

    @PutMapping("/{voucherId}")
    public ApiResponse<VoucherResponse> updateVoucher(@PathVariable Long voucherId, @RequestBody VoucherUpdateRequest request){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.updateVoucher(voucherId, request))
                .build();
    }
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ApiResponse.<String>builder()
                .result("Voucher deleted successfully")
                .build();
    }
    @GetMapping("/{id}")
    public ApiResponse<VoucherResponse> getVoucherById(@PathVariable Long id) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucherById(id))
                .build();
    }

    @GetMapping("/code/{code}")
    public ApiResponse<VoucherResponse> getVoucherByCode(@PathVariable String code) {
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getVoucherByCode(code))
                .build();
    }

    @GetMapping
    public ApiResponse<List<VoucherResponse>> getAllVouchers() {
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getAllVouchers())
                .build();
    }

    @PostMapping("/apply")
    public ApiResponse<ApplyVoucherResponse> applyVoucher(
            @Valid @RequestBody ApplyVoucherRequest request) {
        return ApiResponse.<ApplyVoucherResponse>builder()
                .result(voucherService.applyVoucher(request))
                .message("Voucher applied successfully")
                .build();
    }
}
