package com.powercess.printersystem.printersystem.exception;

import com.powercess.printersystem.printersystem.dto.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleException(Exception e) {
        return ResponseResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
    // 可以添加更多特定异常的处理逻辑，如参数校验失败等
}