package com.hikiw.ecommerce.module.shop.dto;


import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopCreationRequest {
    @NotBlank(message = "Shop Name is required")
    String shopName;
    @NotNull(message = "Owner User ID cannot be null")
    Long ownerUserId;
    String description;
    @Pattern(regexp = "^[0-9]{10,20}$", message = "Invalid phone number format")
    String phoneNumber;
    @Email(message = "Invalid email format")
    String email;
    @Size(max = 500, message = "Address must be less than 500 characters")
    String address;

}
