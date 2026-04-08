package com.hikiw.ecommerce.module.address.dto;


import com.hikiw.ecommerce.Enum.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressRequest {

    @NotBlank(message = "Receiver name is required")
    String receiverName;

    @NotBlank(message = "Receiver phone is required")
    @Pattern(regexp = "^(0|\\+84)[3|5|7|8|9][0-9]{8}$", message = "Invalid phone number format")
    String receiverPhone;

    @NotBlank(message = "City is required")
    String city;

    @NotBlank(message = "District is required")
    String district;

    @NotBlank(message = "Ward is required")
    String ward;

    @NotBlank(message = "Street detail is required")
    String streetDetail;

    Boolean isDefault = false;

    AddressType addressType = AddressType.HOME;
}