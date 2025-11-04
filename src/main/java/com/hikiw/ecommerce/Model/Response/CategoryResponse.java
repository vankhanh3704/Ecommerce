package com.hikiw.ecommerce.Model.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Long categoryId;
    String categoryName;

    Long parentId;
    String parentName;

    List<CategoryResponse> children; // Dùng để hiển thị cấu trúc cây: Danh sách các con trực tiếp
}
