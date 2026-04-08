package com.hikiw.ecommerce.module.user.dto;

import com.hikiw.ecommerce.Enum.Gender;
import com.hikiw.ecommerce.module.role.dto.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    Long id;
    String username;
    // XÓA TRƯỜNG PASSWORD Ở ĐÂY NHA! Tuyệt đối không trả về password cho Frontend
    String fullName;
    String email;
    String phoneNumber;
    String avatarUrl;
    LocalDate dateOfBirth;
    Gender gender;

    Set<RoleResponse> roles;
}
