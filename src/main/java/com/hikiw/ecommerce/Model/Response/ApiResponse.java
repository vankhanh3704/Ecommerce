package com.hikiw.ecommerce.Model.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder //cho phép tạo object theo kiểu builder
@JsonInclude(JsonInclude.Include.NON_NULL) //khi convert sang JSON, bỏ qua những trường có giá trị null

// khuôn mẫu cho mọi phản hồi (response) mà API sẽ gửi về client.
public class ApiResponse<T> {
    @Builder.Default
    int code = 1000;

    String message;
    T result; // kiểu trả về không cố định nên là dùng T ( generic)
}
