package com.powercess.printersystem.printersystem.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {
    @Getter
    private final int code;
    private final String message;
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}