package com.hikiw.ecommerce.Entity;


import com.hikiw.ecommerce.Enum.DataType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attribute_key")
public class AttributeKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long attributeKeyId;
    Boolean isActive = true; // Thuộc tính có hoạt động hay không

    String keyName; // e.g., Color, Size
    String displayName; // e.g., Màu sắc, Kích thước
    Boolean isRequired; // Thuộc tính bắt buộc hay không

    @Enumerated(EnumType.STRING)
    DataType dataType = DataType.TEXT; // Kiểu dữ liệu của thuộc tính
    String description; // Mô tả về thuộc tính
    Integer displayOrder = 0; // Thứ tự hiển thị

    @OneToMany(mappedBy = "attributeKey", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductAttributeValueEntity> productAttributeValues = new ArrayList<>();
}
