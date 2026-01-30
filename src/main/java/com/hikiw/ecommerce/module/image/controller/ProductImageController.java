package com.hikiw.ecommerce.module.image.controller;


import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.image.dto.ImageUploadResponse;
import com.hikiw.ecommerce.module.image.service.ProductImageService;
import com.hikiw.ecommerce.module.image.dto.ProductImageUploadRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/product-image")
@RequiredArgsConstructor
public class ProductImageController {
    ProductImageService productImageService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ImageUploadResponse> uploadImageToProduct(
            @RequestParam Long productId,
            @RequestPart("imageFile") MultipartFile imageFile,
            @RequestParam(required = false) Boolean isPrimary,
            @RequestParam(required = false) String altText
    ) throws IOException {

        ProductImageUploadRequest request = ProductImageUploadRequest.builder()
                .productId(productId)
                .imageFile(imageFile)
                .isPrimary(isPrimary)
                .altText(altText)
                .build();
        ImageUploadResponse response = productImageService.uploadImageToProduct(request);
        return ApiResponse.<ImageUploadResponse>builder()
                .result(response)
                .build();

    }

}
