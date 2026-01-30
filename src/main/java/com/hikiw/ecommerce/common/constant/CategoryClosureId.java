package com.hikiw.ecommerce.common.constant;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Embeddable // Đánh dấu là một lớp Khóa Chính có thể được nhúng vào entity khác
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // Bắt buộc trong khóa tổng hợp — giúp JPA so sánh đúng các bản ghi composite key.
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryClosureId implements Serializable {
    // Lưu ý: Tên trường phải khớp với tên trường trong CategoryClosureEntity
    @Column(name = "ancestor_id")
    Long ancestor;
    @Column(name = "descendant_id")
    Long descendant;

}
