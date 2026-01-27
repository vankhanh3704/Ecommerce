package com.hikiw.ecommerce.Entity;


import com.hikiw.ecommerce.Enum.Datatype;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;

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

    String keyName; // e.g., Color, Size
    String displayName; // e.g., Màu sắc, Kích thước
    Boolean isRequired; // Thuộc tính bắt buộc hay không

    @Enumerated(EnumType.STRING)
    Datatype dataType = Datatype.TEXT; // Kiểu dữ liệu của thuộc tính

    Integer displayOrder = 0; // Thứ tự hiển thị

    @OneToMany(mappedBy = "attributeKey", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductAttributeValueEntity> productAttributeValues = new ArrayList<>();
}
