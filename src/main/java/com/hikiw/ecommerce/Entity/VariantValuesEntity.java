package com.hikiw.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;


// Giá trị của biến thể (ví dụ: nhỏ, vừa, lớn cho kích thước; đỏ, xanh cho màu sắc)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "variant_values")
@Getter
@Setter
public class VariantValuesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long variantValueId;

    String valueName;
    Integer displayOrder = 0; // Lower values indicate higher priority : hiển thị thứ tự ưu tiên
    Boolean isActive = true;
    String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    VariantEntity variant;

    @OneToMany(mappedBy = "variantValue", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductVariantMappingEntity> productVariantMappings = new ArrayList<>();
}
