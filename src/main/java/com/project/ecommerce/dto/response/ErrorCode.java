package com.project.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    USER_EXISTS(404, "User already exists"),
    USER_NOT_FOUND(404, "User not found"),
    USER_CREATE_SUCCESS(200, "User created successfully"),
    DELETE_USER_SUCCESS(200, "User deleted successfully"),
    DELETE_USER_FAIL(400, "User deleted successfully"),
    EMAIL_EXISTS(400, "Email already exists"),
    USER_AND_EMAIL_EXISTS(400, "User and email already exists"),
    USERNAME_EXISTS(400, "Username already exists"),
    NO_USER(400, "User clear"),
    DELETE_ALL(200, "Delete all users"),
    ;
    private int code;
    private String message;

}
