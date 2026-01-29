package com.hikiw.ecommerce.Service;


import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
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
    public String uploadImage(MultipartFile file) throws IOException {
        log.info("Uploading image to Cloudinary: {}", file.getOriginalFilename());

        // Validate file
        validateImageFile(file);


        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "ecommerce/products", // Thư mục Cloudinary : không cần public_id để nó tự sinh
                        "resource_type", "image", // chỉ rõ là ảnh
                        "transformation", ObjectUtils.asMap( // Optimize ảnh ngay khi upload
                                "quality", "auto",
                                "fetch_format", "auto"
                        )
                ));

        // Get secure URL
        String imageUrl = (String) uploadResult.get("secure_url"); //secure_url = HTTPS

        log.info("Image uploaded successfully: {}", imageUrl);
        return imageUrl;
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
    private Transformation<?> productThumbnail(int size) {
        return new Transformation<>()
                .width(size)
                .height(size)
                .crop("fill")
                .quality("auto")
                .fetchFormat("auto");
    }

    public String getOptimizedUrl(String imageUrl, int width, int height) {
        String publicId = extractPublicIdFromUrl(imageUrl);
        if (publicId == null) {
            return imageUrl;
        }

        return cloudinary.url()
                .secure(true)
                .transformation(productThumbnail(300))
                .generate(publicId);
    }

}
