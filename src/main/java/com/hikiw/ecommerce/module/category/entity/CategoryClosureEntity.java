package com.hikiw.ecommerce.module.category.entity;


import com.hikiw.ecommerce.common.constant.CategoryClosureId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "category_closure")
public class CategoryClosureEntity {
    @EmbeddedId // bắt buộc dùng với khoá tổng hợp
    CategoryClosureId id;

    @Column(nullable = false)
    Integer depth;

    // Mối quan hệ với category (Tổ tiên)
    @ManyToOne
    @MapsId("ancestor")
    @JoinColumn(name = "ancestor_id")
    @ToString.Exclude // tránh vòng lặp vô hạn (Loại trừ đối tượng Category Tổ tiên)
    CategoryEntity ancestor;

    // Mối quan hệ với category ( Hậu duệ)
    @ManyToOne
    @MapsId("descendant")
    @JoinColumn(name = "descendant_id")
    @ToString.Exclude //  Loại trừ đối tượng Category Hậu duệ
    CategoryEntity descendant;
}
