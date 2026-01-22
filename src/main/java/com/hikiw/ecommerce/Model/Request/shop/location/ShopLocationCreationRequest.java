package com.hikiw.ecommerce.Model.Request.shop.location;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShopLocationCreationRequest {

    Long shopId;

    String contactName;

    String phoneNumber;

    String street;

    String ward;

    String district;

    String city;

    String country;

    String postalCode;

    Boolean isDefaultPickup;

    Boolean isDefaultReturn;

    String locationName;

    String note;
}
