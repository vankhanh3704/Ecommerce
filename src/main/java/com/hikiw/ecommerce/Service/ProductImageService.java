package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Entity.ProductEntity;
import com.hikiw.ecommerce.Entity.ProductImageEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Model.Request.product.image.ProductImageUploadRequest;
import com.hikiw.ecommerce.Model.Response.CloudinaryUploadResult;
import com.hikiw.ecommerce.Model.Response.image.ImageUploadResponse;
import com.hikiw.ecommerce.Model.Response.image.ProductImageResponse;
import com.hikiw.ecommerce.Repository.ProductImageRepository;
import com.hikiw.ecommerce.Repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ProductImageService {
    ProductImageRepository productImageRepository;
    CloudinaryService cloudinaryService;
    ProductRepository productRepository;
    String CLOUDINARY_FOLDER = "ecommerce/products";

    @Transactional
    public ImageUploadResponse uploadImageToProduct(ProductImageUploadRequest request) throws IOException {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        // Upload to Cloudinary
        CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(
                request.getImageFile(),
                CLOUDINARY_FOLDER
        );

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
}
