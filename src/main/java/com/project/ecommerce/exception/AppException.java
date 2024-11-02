package com.project.ecommerce.exception;

import com.project.ecommerce.dto.response.ErrorCode;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
