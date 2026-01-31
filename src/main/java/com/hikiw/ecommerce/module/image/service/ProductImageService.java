package com.hikiw.ecommerce.module.image.service;


import com.hikiw.ecommerce.module.image.dto.*;
import com.hikiw.ecommerce.module.image.entity.ProductImageEntity;
import com.hikiw.ecommerce.module.image.mapper.ProductImageMapper;
import com.hikiw.ecommerce.module.image.repository.ProductImageRepository;
import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.common.Exception.AppException;
import com.hikiw.ecommerce.common.Response.CloudinaryUploadResult;
import com.hikiw.ecommerce.module.product.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ProductImageService {
    ProductImageRepository productImageRepository;
    CloudinaryService cloudinaryService;
    ProductRepository productRepository;
    String CLOUDINARY_FOLDER = "ecommerce/products";
    ProductImageMapper productImageMapper;

    @Transactional
    public ImageUploadResponse uploadImageToProduct(ProductImageUploadRequest request) throws IOException {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Upload to Cloudinary
        CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(request.getImageFile(), CLOUDINARY_FOLDER);

        // If setPrimary = true, unset other primary images
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            productImageRepository.unsetAllPrimaryImages(request.getProductId());
        }

        long count = productImageRepository.countByProduct_ProductId(request.getProductId());

        ProductImageEntity productImage = ProductImageEntity.builder()
                .product(product)
                .imageUrl(uploadResult.getSecureUrl())
                .cloudinaryPublicId(uploadResult.getPublicId())
                .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
                .displayOrder((int) count)
                .altText(request.getAltText())
                .imageSize(uploadResult.getBytes())
                .imageWidth(uploadResult.getWidth())
                .imageHeight(uploadResult.getHeight())
                .build();

        ProductImageEntity saved = productImageRepository.save(productImage);


        return ImageUploadResponse.builder()
                .imageId(saved.getImageId())
                .imageUrl(saved.getImageUrl())
                .cloudinaryPublicId(saved.getCloudinaryPublicId())
                .fileName(request.getImageFile().getOriginalFilename())
                .fileSize(uploadResult.getBytes())
                .width(uploadResult.getWidth())
                .height(uploadResult.getHeight())
                .contentType(request.getImageFile().getContentType())
                .isPrimary(saved.getIsPrimary())
                .thumbnailUrl(cloudinaryService.getThumbnailUrl(saved.getCloudinaryPublicId()))
                .mediumUrl(cloudinaryService.getMediumUrl(saved.getCloudinaryPublicId()))
                .build();
    }

    @Transactional
    public List<ImageUploadResponse> uploadMultipleImages(ProductImageBatchUploadRequest request) throws IOException {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ImageUploadResponse> imageUploadResponses = new ArrayList<>();
        long count = productImageRepository.countByProduct_ProductId(request.getProductId());
        for(int i = 0; i < request.getImageFiles().size(); i++) {
            MultipartFile multipartFile = request.getImageFiles().get(i);
            try{
                CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(multipartFile, CLOUDINARY_FOLDER);

                ProductImageEntity  productImage = ProductImageEntity.builder()
                        .product(product)
                        .imageUrl(uploadResult.getSecureUrl())
                        .imageSize(uploadResult.getBytes())
                        .imageHeight(uploadResult.getHeight())
                        .displayOrder((int)count)
                        .cloudinaryPublicId(uploadResult.getPublicId())
                        .isPrimary(false)
                        .imageWidth(uploadResult.getWidth())
                        .build();


                ProductImageEntity saved = productImageRepository.save(productImage);


                imageUploadResponses.add(ImageUploadResponse.builder()
                        .imageId(saved.getImageId())
                        .imageUrl(saved.getImageUrl())
                        .cloudinaryPublicId(saved.getCloudinaryPublicId())
                        .fileName(multipartFile.getOriginalFilename())
                        .fileSize(uploadResult.getBytes())
                        .width(uploadResult.getWidth())
                        .height(uploadResult.getHeight())
                        .contentType(multipartFile.getContentType())
                        .isPrimary(saved.getIsPrimary())
                        .thumbnailUrl(cloudinaryService.getThumbnailUrl(saved.getCloudinaryPublicId()))
                        .mediumUrl(cloudinaryService.getMediumUrl(saved.getCloudinaryPublicId()))
                        .build());

            }catch (Exception e){
                throw new AppException(ErrorCode.UPLOAD_PRODUCT_IMAGE_FAILED);
            }
        }
        return imageUploadResponses;
    }


    // set primary image
    @Transactional
    public ProductImageResponse setPrimaryImage(Long imageId){
        ProductImageEntity productImage = productImageRepository
                .findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXITED));

        // phải unset All tất cả primary của product trước
        productImageRepository.unsetAllPrimaryImages(productImage.getProduct().getProductId());
        productImage.setIsPrimary(true);
        productImageRepository.save(productImage);
        return toResponseTransformations(productImage);
    }


    // update product image
    @Transactional
    public ProductImageResponse updateProductImage(Long imageId, ProductImageUpdateRequest request){
        ProductImageEntity entity = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXITED));

        if(Boolean.TRUE.equals(request.getIsPrimary())){
            productImageRepository.unsetAllPrimaryImages(entity.getProduct().getProductId());
        }

        productImageMapper.toUpdate(entity, request);
        ProductImageEntity saved = productImageRepository.save(entity);
        return toResponseTransformations(saved);

    }

    @Transactional(readOnly = true)
    public ProductImagesResponse getProductImages(Long productId){
        ProductEntity product = productRepository
                .findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        List<ProductImageEntity> productImages = productImageRepository.findByProduct_ProductIdOrderByDisplayOrderAsc(productId);

        // lọc primary image
        ProductImageResponse primaryImage = productImages.stream()
                .filter(ProductImageEntity::getIsPrimary) // kiểm tra từng ảnh và chỉ giữ lại primary = true
                .findFirst()
                .map(this::toResponseTransformations)
                .orElse(null);

        List<ProductImageResponse> productImagesResponse = productImages.stream()
                .map(this::toResponseTransformations)
                .toList();

        return ProductImagesResponse.builder()
                .images(productImagesResponse)
                .primaryImage(primaryImage)
                .totalImages(productImages.size())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .build();
    }

    @Transactional(readOnly = true)
    public ProductImageResponse getProductImageById(Long imageId){
        ProductImageEntity productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXITED));

        return toResponseTransformations(productImage);
    }

    // phải xoá trên cloud trước xong mới xoá
    @Transactional
    public void deleteProductImage(Long imageId) throws IOException {
        ProductImageEntity productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_IMAGE_NOT_EXITED));

        if(productImage.getCloudinaryPublicId() != null){
            cloudinaryService.deleteImage(productImage.getCloudinaryPublicId());
        }
        productImageRepository.delete(productImage);
    }

    @Transactional
    public void deleteAllProductImages(Long productId) throws IOException {

        List<ProductImageEntity> images = productImageRepository.findByProduct_ProductIdOrderByDisplayOrderAsc(productId);

        for (ProductImageEntity image : images) {
            if (image.getCloudinaryPublicId() != null) {
                cloudinaryService.deleteImage(image.getCloudinaryPublicId());
            }
        }
        productImageRepository.deleteByProduct_ProductId(productId);
    }



    public ProductImageResponse toResponseTransformations(ProductImageEntity productImage){
        ProductImageResponse productImageResponse = productImageMapper.toResponse(productImage);

        if(productImage.getCloudinaryPublicId() != null){
            productImageResponse.setMediumUrl(cloudinaryService.getMediumUrl(productImage.getCloudinaryPublicId()));
            productImageResponse.setThumbnailUrl(cloudinaryService.getThumbnailUrl(productImage.getCloudinaryPublicId()));
        }

        return productImageResponse;
    }
}
