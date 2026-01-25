package com.hikiw.ecommerce.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class ShopDetailResponse {
    Long shopId;
    Long ownerUserId;
    String ownerName;
    String shopName;
    String description;
    String address;
    String phoneNumber;
    String email;
    Double rating;
    Integer totalProducts;
    Integer totalOrders;
    Boolean isVerified;
    Boolean isActive;

    // Danh sách địa điểm
    List<ShopLocationResponse> shopLocations;

}
