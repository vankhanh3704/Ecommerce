package com.hikiw.ecommerce.module.category.controller;


import com.hikiw.ecommerce.module.category.dto.CategoryCreationRequest;
import com.hikiw.ecommerce.module.category.dto.MoveCategoryRequest;
import com.hikiw.ecommerce.common.Response.ApiResponse;
import com.hikiw.ecommerce.module.category.dto.CategoryBreadCrumbsResponse;
import com.hikiw.ecommerce.module.category.dto.CategoryResponse;
import com.hikiw.ecommerce.module.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreationRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getCategoryDetails(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder().result("Delete category successful").build();
    }

    @PutMapping("/{categoryId}/move")
    public ApiResponse<String> moveCategory(@PathVariable Long categoryId, @RequestBody MoveCategoryRequest request){
        categoryService.moveCategory(categoryId, request.getNewParentId());
        return ApiResponse.<String>builder().result("Move category successful").build();
    }

    @GetMapping("/{id}/breadcrumbs")
    public ApiResponse<List<CategoryBreadCrumbsResponse>> getCategoryBreadcrumbs(@PathVariable Long id){
        return ApiResponse.<List<CategoryBreadCrumbsResponse>>builder()
                .result(categoryService.getCategoryBreadcrumbs(id))
                .build();
    }

    @GetMapping("/root")
    public ApiResponse<List<CategoryResponse>> getRootCategories() {
        List<CategoryResponse> roots = categoryService.getRootCategories();
        return ApiResponse.<List<CategoryResponse>> builder().result(roots).build();
    }

    @GetMapping("/{parentId}/children")
    public ApiResponse<List<CategoryResponse>> getChildrenCategories(@PathVariable Long parentId){
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getChildrenCategories(parentId))
                .build();
    }
}

