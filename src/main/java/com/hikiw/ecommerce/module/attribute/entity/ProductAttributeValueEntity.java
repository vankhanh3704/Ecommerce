package com.hikiw.ecommerce.module.attribute.entity;


import com.hikiw.ecommerce.module.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Table(name = "product_attribute_value")
public class ProductAttributeValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long pavId;

    @ManyToOne
    @JoinColumn(name = "attribute_key_id", nullable = false)
    AttributeKeyEntity attributeKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    ProductEntity product;

    String valueText; // Giá trị thuộc tính dạng văn bản
}
