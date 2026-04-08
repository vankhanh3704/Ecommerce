package com.hikiw.ecommerce.module.user.dto;

import com.hikiw.ecommerce.Enum.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank
    String username;
    @NotBlank String password;
    String fullName;
    String email;
    String phoneNumber;
    Gender gender;
    // Admin có quyền chỉ định role ngay lúc tạo (Ví dụ: truyền vào ["ADMIN", "USER"])
    Set<String> roles;


}
