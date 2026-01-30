package com.hikiw.ecommerce.module.category.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreationRequest {

    String categoryName;
    Long parentId; // parentId sẽ là NULL nếu đây là danh mục cấp gốc
}
