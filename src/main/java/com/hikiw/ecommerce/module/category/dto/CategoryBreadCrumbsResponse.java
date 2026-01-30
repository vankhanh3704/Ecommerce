package com.hikiw.ecommerce.module.category.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryBreadCrumbsResponse {
    Long categoryId;
    String categoryName;
    Long parentId;
}
