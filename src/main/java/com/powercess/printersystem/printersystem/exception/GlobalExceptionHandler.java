package com.powercess.printersystem.printersystem.exception;

import com.powercess.printersystem.printersystem.dto.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<String> handleBusinessException(BusinessException ex) {
        return ResponseResult.error(ex.getCode(), ex.getMessage());
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseResult<String> handleSecurityException(SecurityException ex) {
        return ResponseResult.error(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
    @ExceptionHandler(java.io.FileNotFoundException.class)
    public ResponseResult<String> handleFileNotFoundException(java.io.FileNotFoundException ex) {
        return ResponseResult.error(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseResult.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleUnexpectedException(Exception ex) {
        ex.printStackTrace();
        return ResponseResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误：" + ex.getMessage());
    }
}