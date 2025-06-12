package com.powercess.printersystem.printersystem.service;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.model.User;
public interface UserService {
    ResponseResult<User> register(String account, String username, String password, String phone, String email);
    ResponseResult<?> login(String account, String password);
    ResponseResult<User> getCurrentUserInfo();
    ResponseResult<User> updateUserInfo(String username, String phone, String email);
    ResponseResult<?> changePassword(String oldPassword, String newPassword);
    ResponseResult<?> getExpAndLevel();
}