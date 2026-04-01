package com.hikiw.ecommerce.Enum;

public enum OrderStatus {
    PENDING,    // Chờ xác nhận
    CONFIRMED,  // Đã xác nhận
    SHIPPING,   // Đang giao hàng
    DELIVERED,  // Đã giao hàng
    CANCELLED   // Đã hủy
}
