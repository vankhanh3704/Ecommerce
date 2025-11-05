package com.hikiw.ecommerce.Controller;


import com.hikiw.ecommerce.Model.Request.CategoryCreateRequest;
import com.hikiw.ecommerce.Model.Response.ApiResponse;
import com.hikiw.ecommerce.Model.Response.CategoryResponse;
import com.hikiw.ecommerce.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request){
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


}

