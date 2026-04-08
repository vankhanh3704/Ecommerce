package com.hikiw.ecommerce.module.address.repository;

import com.hikiw.ecommerce.module.address.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    // Lấy danh sách địa chỉ của User, xếp cái Default lên đầu tiên
    List<AddressEntity> findByUser_IdOrderByIsDefaultDesc(Long userId);

    // Lấy 1 địa chỉ cụ thể của đúng User đó (Tránh vụ sửa trộm địa chỉ người khác)
    Optional<AddressEntity> findByIdAndUser_Id(Long id, Long userId);

    // Tìm xem User này đang có địa chỉ nào làm Default không
    Optional<AddressEntity> findByUser_IdAndIsDefaultTrue(Long userId);

    // Kiểm tra xem User đã có địa chỉ nào trong sổ chưa
    boolean existsByUser_Id(Long userId);
}