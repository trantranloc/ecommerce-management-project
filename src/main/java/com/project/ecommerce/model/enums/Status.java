package com.project.ecommerce.model.enums;

import lombok.Getter;

// Java code
@Getter
public enum Status {
    PENDING("Đang sử lý "),
    CONFIRMED("Đã xác nhận"),
    DELIVERY("Đang giao hàng"),
    SHIPPED("Đã giao hàng"),
    CANCELLED("Đã hủy");

    private final String label;

    Status(String label) {
        this.label = label;
    }

}