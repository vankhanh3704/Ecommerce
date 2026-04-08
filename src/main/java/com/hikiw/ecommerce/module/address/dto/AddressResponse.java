package com.hikiw.ecommerce.module.address.dto;


import com.hikiw.ecommerce.Enum.AddressType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    Long id;
    String receiverName;
    String receiverPhone;
    String city;
    String district;
    String ward;
    String streetDetail;

    // Thuộc tính ảo: Nối chuỗi sẵn cho Frontend đỡ phải tự map
    String fullAddress;

    Boolean isDefault;
    AddressType addressType;
}