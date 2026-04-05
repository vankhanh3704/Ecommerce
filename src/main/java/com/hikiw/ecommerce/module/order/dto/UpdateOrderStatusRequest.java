package com.hikiw.ecommerce.module.order.dto;


import com.hikiw.ecommerce.Enum.OrderStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderStatusRequest {
    OrderStatus newStatus; // Trạng thái mới, có thể là "PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELED"
    String note; // Ghi chú về việc cập nhật trạng thái (nếu có)
}
