package com.hikiw.ecommerce.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "variants")

// Biến thể sản phẩm (ví dụ: kích thước, màu sắc)
public class VariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    Long variantId;

    String variantName;
    Integer displayOrder = 0; // Lower values indicate higher priority : hiển thị thứ tự ưu tiên


    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    List<VariantValuesEntity> variantValues = new ArrayList<>(); // tránh null pointer
}
