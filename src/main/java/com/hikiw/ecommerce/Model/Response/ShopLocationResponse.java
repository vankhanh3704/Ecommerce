package com.hikiw.ecommerce.Model.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ShopLocationResponse {
    Integer shopLocationId;
    Integer shopId;
    String shopName;

    // Contact Info
    String contactName;
    String phoneNumber;

    // Address Details
    String street;
    String ward;
    String district;
    String city;
    String country;
    String postalCode;
    String fullAddress;

    // Business Flags
    Boolean isDefaultPickup;
    Boolean isDefaultReturn;
    Boolean isActive;

    // Additional Info
    String locationName;
    String note;

}
