package com.hikiw.ecommerce.Service;

import com.hikiw.ecommerce.Entity.CategoryClosureEntity;
import com.hikiw.ecommerce.Entity.CategoryEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.CategoryMapper;
import com.hikiw.ecommerce.Model.Request.CategoryCreateRequest;
import com.hikiw.ecommerce.Model.Response.CategoryBreadCrumbsResponse;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public CategoryResponse getCategoryDetails(Long categoryId){
        CategoryEntity currentCategory = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        CategoryResponse response = categoryMapper.toCategoryResponse(currentCategory);
        // Xử lý Parent : Tìm và gán tên cha nếu có
        List<CategoryClosureEntity> parentRelation = categoryClosureRepository
                .findAllByDescendant_CategoryIdAndDepth(categoryId, 1)
                .stream().filter(rel -> !rel.getAncestor().equals(currentCategory)) // lọc các thằng (ancestor) tổ tiên mà khác với danh mục hiện tại
                .collect(Collectors.toList());

        if(!parentRelation.isEmpty()){
            CategoryEntity parentEntity = parentRelation.getFirst().getAncestor();
            response.setParentId(parentEntity.getCategoryId());
            response.setParentName(parentEntity.getCategoryName());
        }

        // Xử lý Children: Tìm các con trực tiếp ( depth = 1)
        List<CategoryClosureEntity> childrenRelation = categoryClosureRepository
                .findAllByAncestor_CategoryIdAndDepth(categoryId, 1);

        List<CategoryResponse> childrenResponses = childrenRelation.stream()
                .filter(rel -> rel.getDepth() > 0)
                .map(CategoryClosureEntity::getDescendant)
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        response.setChildren(childrenResponses);
        return response;
    }



    // xoá danh mục
    @Transactional
    public void deleteCategory(Long id){
        if(!categoryRepository.existsById(id)){
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        // 2. [QUAN TRỌNG] Kiểm tra ràng buộc dữ liệu (Ví dụ: Sản phẩm đang sử dụng)
        // if (productRepository.existsByCategory_Id(categoryId)) {
        //     throw new AppException(ErrorCode.CATEGORY_HAS_PRODUCT);
        // }

        // Xóa tất cả các mối quan hệ liên quan trong Closure Table
        categoryClosureRepository.deleteAllByAncestor_CategoryId(id);
        categoryClosureRepository.deleteAllByDescendant_CategoryId(id);
        categoryRepository.deleteById(id);
    }


    @Transactional // BẮT BUỘC phải có @Transactional
    public void moveCategory(Long categoryId, Long newParentId) {

        // 1. Kiểm tra tồn tại và xác thực cơ bản
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        if (newParentId != null && !categoryRepository.existsById(newParentId)) {
            throw new AppException(ErrorCode.PARENT_CATEGORY_NOT_EXISTED); // Tự định nghĩa lỗi này
        }
        if (newParentId != null && categoryId.equals(newParentId)) {
            throw new AppException(ErrorCode.INVALID_MOVE_TO_SELF); // Tự định nghĩa lỗi này
        }

        // 2. NGĂN CHẶN LẶP VÒNG (Không được di chuyển vào chính cây con của nó)
        if (newParentId != null) {
            List<CategoryClosureEntity> movingDescendants = categoryClosureRepository.findAllByAncestor_CategoryId(categoryId);
            boolean isCircularMove = movingDescendants.stream()
                    .anyMatch(rel -> rel.getDescendant().getCategoryId().equals(newParentId));

            if (isCircularMove) {
                throw new AppException(ErrorCode.CANNOT_MOVE_TO_CHILD);
            }
        }

        // --- BƯỚC TỐI ƯU HÓA CLOSURE TABLE BẰNG SQL ---

        // 3. XÓA các mối quan hệ CŨ (Xóa toàn bộ đường dẫn từ tổ tiên cũ đến cây con đang di chuyển)
        categoryClosureRepository.deleteOldPaths(categoryId);

        // 4. CHÈN các mối quan hệ MỚI
        if (newParentId != null) {
            // Trường hợp 1: Di chuyển vào một danh mục cha mới (newParentId)
            categoryClosureRepository.insertNewPaths(categoryId, newParentId);
        } else {
            // Trường hợp 2: Di chuyển thành danh mục gốc (parent = null)
            // KHÔNG làm gì thêm, vì chỉ cần mối quan hệ tự liên kết (depth=0) đã được giữ lại
        }
    }


    @Transactional
    public List<CategoryResponse> getRootCategories() {

        // 1. Tìm TẤT CẢ Category ID là con của một danh mục nào đó (Depth = 1)
        List<Long> allChildIds = categoryClosureRepository.findAllNonRootIds();

        // 2. Lấy những Category có ID KHÔNG nằm trong danh sách con (Tức là danh mục gốc)
        // Lưu ý: findAllChildIds có thể trả về chính nó (depth=0), ta chỉ cần lấy những thằng
        // không phải là hậu duệ của ai khác ngoài chính nó.
        List<CategoryEntity> rootEntities = categoryRepository.findByCategoryIdNotIn(allChildIds);

        // 3. Chuyển đổi sang Response DTO và trả về
        return rootEntities.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    // hàm xử lý lấy ra breadcrumbs của danh mục
    public List<CategoryBreadCrumbsResponse> getCategoryBreadcrumbs(Long categoryId) {

        // 1. Lấy tất cả các dòng quan hệ Tổ tiên/Hậu duệ (bao gồm cả chính nó, depth=0)
        List<CategoryClosureEntity> closureRels =
                categoryClosureRepository.findAllByDescendant_CategoryId(categoryId);

        return closureRels.stream()
                // 2. Map sang đối tượng CategoryEntity Tổ tiên
                .map(CategoryClosureEntity::getAncestor)
                // 3. Chuyển đổi thành Response DTO
                .map(categoryMapper::toCategoryBreadCrumbsResponse)
                // 4. (Tùy chọn) Sắp xếp theo depth để có thứ tự từ Gốc -> Hiện tại
                .sorted(Comparator.comparing(c -> c.getParentId() == null ? 0 : 1)) // Sắp xếp phức tạp hơn
                .collect(Collectors.toList());
    }


    @Transactional
    public List<CategoryResponse> getChildrenCategories(Long parentId){
        if(!categoryRepository.existsById(parentId)){
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }

        // 2. Dùng Closure Table tìm các mối quan hệ con trực tiếp (Depth = 1)
        List<CategoryClosureEntity> childrenRelations = categoryClosureRepository.findAllByAncestor_CategoryIdAndDepth(parentId, 1);

        return childrenRelations.stream()
                .map(CategoryClosureEntity::getDescendant) // lấy catogory của con
                .map(categoryMapper::toCategoryResponse)
                .peek(
                        // Tối ưu: Gắn ID cha cho Response của con (giúp frontend dễ xử lý)
                        categoryResponse -> categoryResponse.setParentId(parentId)
                )
                .collect(Collectors.toList());
    }
}