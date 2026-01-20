package com.hikiw.ecommerce.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShopUpdateRequest {
    String shopName;
    String description;
    String phoneNumber;
    String email;
    String address;
    Boolean isActive;

}
