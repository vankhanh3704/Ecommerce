package com.hikiw.ecommerce.common.Response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudinaryUploadResult {
    private String publicId;
    private String url;
    private String secureUrl;
    private Integer width;
    private Integer height;
    private String format;
    private String resourceType;
    private Long bytes;
}
