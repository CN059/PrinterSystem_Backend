package com.powercess.printersystem.printersystem.dto;

import lombok.Data;
@Data
public class ResponseResult<T> {
    private boolean ifSuccess;
    private int status;
    private String response;
    private T data;
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setIfSuccess(true);
        result.setStatus(200);
        result.setResponse("操作成功");
        result.setData(data);
        return result;
    }
    public static <T> ResponseResult<T> error(int status, String message) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setIfSuccess(false);
        result.setStatus(status);
        result.setResponse(message);
        return result;
    }
}