package com.hikiw.ecommerce.module.authenticate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}
