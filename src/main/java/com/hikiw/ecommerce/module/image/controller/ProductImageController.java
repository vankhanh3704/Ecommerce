package com.hikiw.ecommerce.module.image.controller;


import com.cloudinary.Api;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.image.dto.*;
import com.hikiw.ecommerce.module.image.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/product-image")
@RequiredArgsConstructor
public class ProductImageController {
    ProductImageService productImageService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ImageUploadResponse> uploadImageToProduct(
            @ModelAttribute ProductImageUploadRequest request
    ) throws IOException {
        return ApiResponse.<ImageUploadResponse>builder()
                .result(productImageService.uploadImageToProduct(request))
                .build();

    }


    @RequestMapping(value = "/upload/multiple", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<ImageUploadResponse>> uploadMultipleImages(
            @ModelAttribute ProductImageBatchUploadRequest request
    ) throws IOException {
        return ApiResponse.<List<ImageUploadResponse>>builder()
                .result(productImageService.uploadMultipleImages(request))
                .build();
    }

    @PutMapping("/{imageId}")
    public ApiResponse<ProductImageResponse> setPrimaryImage(@PathVariable("imageId") Long id){
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.setPrimaryImage(id))
                .build();
    }

    @PutMapping("/update/{imageId}")
    public ApiResponse<ProductImageResponse> updateProductImage(@PathVariable("imageId") Long id, @RequestBody ProductImageUpdateRequest request){
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.updateProductImage(id, request))
                .build();
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ApiResponse<ProductImagesResponse> getProductImages(@PathVariable("productId") Long productId){
        return ApiResponse.<ProductImagesResponse>builder()
                .result(productImageService.getProductImages(productId))
                .build();
    }

    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET)
    public ApiResponse<ProductImageResponse> getProductImageById(@PathVariable("imageId") Long id){
        return ApiResponse.<ProductImageResponse>builder()
                .result(productImageService.getProductImageById(id))
                .build();
    }

    @RequestMapping(value = "/{imageId}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteProductImage(@PathVariable("imageId") Long id) throws IOException {
        productImageService.deleteProductImage(id);
        return ApiResponse.<Void>builder()
                .message("Delete product image successfully")
                .build();
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.DELETE)
    public ApiResponse<Void> deleteProductImageByProductId(@PathVariable("productId") Long productId) throws IOException {
        productImageService.deleteAllProductImages(productId);
        return ApiResponse.<Void>builder()
                .message("Delete product image by productId successfully")
                .build();
    }
}
