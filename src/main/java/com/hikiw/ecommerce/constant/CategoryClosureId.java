package com.hikiw.ecommerce.constant;


import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryClosureId implements Serializable {
    // Lưu ý: Tên trường phải khớp với tên trường trong CategoryClosureEntity
    Long ancestor;
    Long descendant;

}
