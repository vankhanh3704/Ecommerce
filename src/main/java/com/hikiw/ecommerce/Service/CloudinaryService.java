package com.hikiw.ecommerce.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.hikiw.ecommerce.Model.Response.CloudinaryUploadResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CloudinaryService {

    Cloudinary cloudinary;


    /**
     * Upload image to Cloudinary:
     * Chức năng : nhận ảnh từ client : MultipartFile
     * validate ảnh
     * upload lên cloud
     * Trả về URL HTTPS của ảnh đã tối ưu
     */
    public CloudinaryUploadResult uploadImage(MultipartFile file, String folder) throws IOException {
        log.info("Uploading image to Cloudinary: {}", file.getOriginalFilename());

        // Validate file
        validateImageFile(file);

        // Generate unique public_id
        String publicId = UUID.randomUUID().toString();

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", folder,
                        "resource_type", "image",
                        "quality", "auto:good",
                        "fetch_format", "auto"
                ));

        // Extract result
        String imageUrl = (String) uploadResult.get("secure_url");
        String actualPublicId = (String) uploadResult.get("public_id");
        Integer width = (Integer) uploadResult.get("width");
        Integer height = (Integer) uploadResult.get("height");
        Long bytes = ((Number) uploadResult.get("bytes")).longValue();

        // Get secure URL
        return CloudinaryUploadResult.builder()
                .publicId(actualPublicId)
                .url(imageUrl)
                .secureUrl(imageUrl)
                .width(width)
                .height(height)
                .format((String) uploadResult.get("format"))
                .resourceType((String) uploadResult.get("resource_type"))
                .bytes(bytes)
                .build();
    }


    /**
     * Delete image from Cloudinary
     */
    public void deleteImage(String imageUrl) throws IOException {
        log.info("Deleting image from Cloudinary: {}", imageUrl);

        // Extract public_id from URL
        String publicId = extractPublicIdFromUrl(imageUrl);

        if (publicId != null) {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted: {}", result.get("result"));
        } else {
            log.warn("Could not extract public_id from URL: {}", imageUrl);
        }
    }

    /**
     * Extract public_id from Cloudinary URL
     * VD: https://res.cloudinary.com/demo/image/upload/v1234/products/abc.jpg
     * → products/abc
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // Split URL by "/"
            String[] parts = imageUrl.split("/");

            // Find "upload" index
            int uploadIndex = -1;
            for (int i = 0; i < parts.length; i++) {
                if ("upload".equals(parts[i])) {
                    uploadIndex = i;
                    break;
                }
            }

            if (uploadIndex == -1 || uploadIndex + 2 >= parts.length) {
                return null;
            }

            // Get public_id (sau "upload" và version)
            StringBuilder publicId = new StringBuilder();
            for (int i = uploadIndex + 2; i < parts.length; i++) {
                if (i > uploadIndex + 2) {
                    publicId.append("/");
                }
                publicId.append(parts[i]);
            }

            // Remove extension
            String result = publicId.toString();
            int dotIndex = result.lastIndexOf(".");
            if (dotIndex > 0) {
                result = result.substring(0, dotIndex);
            }

            return result;
        } catch (Exception e) {
            log.error("Error extracting public_id from URL: {}", imageUrl, e);
            return null;
        }
    }

    /**
     * Validate image file
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Check file size (max 5MB)
        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("File size exceeds 5MB");
        }

        String contentType = file.getContentType();

        // Check content type
        List<String> allowedTypes = List.of(
                "image/jpeg",
                "image/png",
                "image/webp"
        );

        if (!allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Only JPG, PNG, WEBP are allowed");
        }
    }


    /**
     * Get optimized image URL
     * Tự động resize và optimize
     */
    private Transformation<?> productThumbnail(int width, int height, String crop) {
        return new Transformation<>()
                .width(width)
                .height(height)
                .crop(crop)
                .quality("auto")
                .fetchFormat("auto");
    }

    public String getOptimizedUrl(String publicId, int width, int height, String crop) {
        if (publicId == null || publicId.isEmpty()) {
            return null;
        }

        return cloudinary.url()
                .secure(true)
                .transformation(productThumbnail(width,height,crop))
                .generate(publicId);

    }

    public String getThumbnailUrl(String publicId) {
        return getOptimizedUrl(publicId, 200, 200, "fill");
    }

    /**
     * Get medium URL (800x600)
     */
    public String getMediumUrl(String publicId) {
        return getOptimizedUrl(publicId, 800, 600, "fill");
    }

}
