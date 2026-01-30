package com.hikiw.ecommerce.module.category.mapper;

import com.hikiw.ecommerce.module.category.entity.CategoryEntity;
import com.hikiw.ecommerce.module.category.dto.CategoryCreationRequest;
import com.hikiw.ecommerce.module.category.dto.CategoryBreadCrumbsResponse;
import com.hikiw.ecommerce.module.category.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // 1. Request DTO -> Entity
    // Mapper chỉ lo ánh xạ tên. Service sẽ lo lưu vào DB và Closure Table.
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "ancestorRelations", ignore = true)
    @Mapping(target = "descendantRelations", ignore = true)
    CategoryEntity toCategoryEntity(CategoryCreationRequest request);


    // 2. Entity -> Response DTO
    // Dùng Custom Setter method (hoặc Service tự set Response DTO)
    @Mappings({
            @Mapping(source = "categoryId", target = "categoryId"), // Dùng tên trường thực tế
            @Mapping(source = "categoryName", target = "categoryName"),
            @Mapping(target = "parentId", ignore = true),   // Bỏ qua: Sẽ được set trong Service
            @Mapping(target = "parentName", ignore = true), // Bỏ qua: Sẽ được set trong Service
            @Mapping(target = "children", ignore = true)
    })
    CategoryResponse toCategoryResponse(CategoryEntity entity);

    @Mappings({
            @Mapping(source = "categoryId", target = "categoryId"), // Dùng tên trường thực tế
            @Mapping(source = "categoryName", target = "categoryName"),
            @Mapping(target = "parentId", ignore = true),   // Bỏ qua: Sẽ được set trong Service
    })
    CategoryBreadCrumbsResponse toCategoryBreadCrumbsResponse(CategoryEntity entity);
}
