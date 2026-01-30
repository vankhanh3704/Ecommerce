package com.hikiw.ecommerce.module.authenticate.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class LogoutRequest {
    String token;
}
