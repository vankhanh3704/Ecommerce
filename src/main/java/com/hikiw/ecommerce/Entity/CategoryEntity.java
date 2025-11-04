package com.hikiw.ecommerce.Entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long categoryId;
    String categoryName;


    // --- Mối quan hệ với Closure Table (Nên dùng @ToString.Exclude) ---

    // 1. Category này là TỔ TIÊN của những mối quan hệ nào
    @OneToMany(mappedBy = "ancestor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Quan trọng: Ngăn chặn StackOverflowError
    @EqualsAndHashCode.Exclude
    Set<CategoryClosureEntity> ancestorRelations;

    // 2. Category này là HẬU DUỆ của những mối quan hệ nào
    @OneToMany(mappedBy = "descendant", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Quan trọng: Ngăn chặn StackOverflowError
    @EqualsAndHashCode.Exclude
    Set<CategoryClosureEntity> descendantRelations;
}
