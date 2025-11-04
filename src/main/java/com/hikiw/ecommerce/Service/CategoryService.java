package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.CategoryClosureEntity;
import com.hikiw.ecommerce.Entity.CategoryEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.CategoryMapper;
import com.hikiw.ecommerce.Model.Request.CategoryCreateRequest;
import com.hikiw.ecommerce.Model.Response.CategoryResponse;
import com.hikiw.ecommerce.Repository.CategoryClosureRepository;
import com.hikiw.ecommerce.Repository.CategoryRepository;
import com.hikiw.ecommerce.constant.CategoryClosureId;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;
    CategoryClosureRepository categoryClosureRepository;

    CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request){
        CategoryEntity currentCategory = categoryMapper.toCategoryEntity(request);

        if(request.getParentId() != null){
            CategoryEntity parentEntity = categoryRepository
                    .findById(request.getParentId())
                    .orElseThrow( () -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            // (Trong mô hình Closure Table, bạn không cần setParent, nhưng cần ID)
        }

        currentCategory = categoryRepository.save(currentCategory);

        Long currentId = currentCategory.getCategoryId();
        Long parentId = request.getParentId();


        // Logic phức tạp cho Closure Table (Thực thi phần Closure Logic)
        CategoryClosureId categoryClosureId = new CategoryClosureId(currentId, currentId);
        CategoryClosureEntity categoryClosureEntity = new CategoryClosureEntity(categoryClosureId, 0, currentCategory, currentCategory);
        categoryClosureRepository.save(categoryClosureEntity);

        if(parentId != null){
            List<CategoryClosureEntity> ancestorOfParent = categoryClosureRepository.findAllByDescendant_CategoryId(parentId);

            // chèn các mối quan hệ mới
            for(CategoryClosureEntity rel : ancestorOfParent){

                CategoryClosureEntity newRel = new CategoryClosureEntity();

                CategoryEntity ancestorEntity = rel.getAncestor();
                CategoryEntity descendantEntity = currentCategory;

                CategoryClosureId newIdRel = new CategoryClosureId(ancestorEntity.getCategoryId(), descendantEntity.getCategoryId());

                newRel.setId(newIdRel);
                newRel.setDepth(rel.getDepth() + 1);
                newRel.setAncestor(ancestorEntity);
                newRel.setDescendant(descendantEntity);

                categoryClosureRepository.save(newRel);
            }

        }
        // 1. Dùng Mapper để chuyển Entity mới thành Response DTO
        CategoryResponse response = categoryMapper.toCategoryResponse(currentCategory);


        if (parentId != null) {

            // 2. Tìm CategoryEntity cha chỉ để lấy tên
            CategoryEntity parentEntity = categoryRepository.findById(parentId)
                    .orElse(null); // Sử dụng orElse(null) nếu bạn đã kiểm tra lỗi trước đó

            // 3. SET thông tin cha trực tiếp vào Response DTO
            if (parentEntity != null) {
                // Cần đảm bảo rằng bạn đã set cả parentId và parentName,
                // mặc dù parentId đã được Mapper set qua request (nếu bạn set trong DTO)

                response.setParentId(parentId);
                response.setParentName(parentEntity.getCategoryName());
            }
        }

        return response;

    }
}