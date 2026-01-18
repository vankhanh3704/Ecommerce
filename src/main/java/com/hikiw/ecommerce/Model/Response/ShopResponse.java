package com.hikiw.ecommerce.Model.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShopResponse {
    Long shopId;
    String shopName;
    Long ownerUserId;
    String ownerName;
    String description;
    String phoneNumber;
    String email;
    String address;
    Double rating;
    Boolean isActive;
    Boolean isVerified;
    Integer totalProducts;
    Integer totalOrders;

}
