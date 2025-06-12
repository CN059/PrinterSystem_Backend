package com.powercess.printersystem.printersystem.controller;

import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.service.AddressService;
import com.powercess.printersystem.printersystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    // 注册
    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String username = payload.get("username");
        String password = payload.get("password");
        String phone = payload.get("phone");
        String email = payload.get("email");
        return userService.register(account, username, password, phone, email);
    }
    // 登录
    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String password = payload.get("password");
        return userService.login(account, password);
    }
    // 获取当前用户信息
    @GetMapping("/me")
    public ResponseResult<?> getCurrentUserInfo() {
        return userService.getCurrentUserInfo();
    }
    // 修改个人信息
    @PatchMapping("/update")
    public ResponseResult<?> updateUserInfo(@RequestBody Map<String, String> payload) {
        String account = payload.get("account");
        String username = payload.get("username");
        String phone = payload.get("phone");
        String email = payload.get("email");
        return userService.updateUserInfo(account, username, phone, email);
    }
    // 修改密码
    @PostMapping("/change-password")
    public ResponseResult<?> changePassword(@RequestBody Map<String, String> payload) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");
        return userService.changePassword(oldPassword, newPassword);
    }
    // 获取经验值和等级
    @GetMapping("/exp")
    public ResponseResult<?> getExpAndLevel() {
        return userService.getExpAndLevel();
    }
    // 获取地址列表
    @GetMapping("/addresses")
    public ResponseResult<?> getAddresses() {
        return addressService.getAddresses();
    }
    // 添加地址
    @PostMapping("/address/add")
    public ResponseResult<?> addAddress(@RequestBody Map<String, Object> payload) {
        String fullAddress = (String) payload.get("fullAddress");
        String receiverName = (String) payload.get("receiverName");
        String receiverPhone = (String) payload.get("receiverPhone");
        Boolean isDefault = (Boolean) payload.get("isDefault");
        return addressService.addAddress(fullAddress, receiverName, receiverPhone, isDefault);
    }
    // 设置默认地址
    @PatchMapping("/address/set-default/{addressId}")
    public ResponseResult<?> setDefaultAddress(@PathVariable("addressId") Long addressId) {
        return addressService.setDefaultAddress(addressId);
    }
}