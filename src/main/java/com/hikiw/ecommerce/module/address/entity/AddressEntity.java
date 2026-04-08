package com.hikiw.ecommerce.module.address.entity;

import com.hikiw.ecommerce.Enum.AddressType;
// Import BaseEntity nếu bạn có dùng, nếu không thì xóa dòng này đi:
// import com.hikiw.ecommerce.common.entity.BaseEntity;
import com.hikiw.ecommerce.common.constant.BaseEntity;
import com.hikiw.ecommerce.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "address")
public class AddressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Column(name = "receiver_name", nullable = false)
    String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    String receiverPhone;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String ward;

    @Column(name = "street_detail", nullable = false)
    String streetDetail; // Số nhà, tên đường, ngõ ngách

    @Builder.Default
    @Column(name = "is_default")
    Boolean isDefault = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    AddressType addressType;
}