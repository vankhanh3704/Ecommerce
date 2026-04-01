package com.hikiw.ecommerce.module.order.dto;


import com.hikiw.ecommerce.Enum.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusHistoryResponse {
    Long historyId;
    OrderStatus status;
    String note;
    LocalDateTime createdDate;
}
