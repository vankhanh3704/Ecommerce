package com.hikiw.ecommerce.module.user.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserUpdateRequest {
    String fullName;
    String email;
    String phoneNumber;
    Set<String> roles;
}
