package com.powercess.printersystem.printersystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.LoginDTO;
import com.powercess.printersystem.printersystem.dto.LoginResponseDTO;
import com.powercess.printersystem.printersystem.dto.RegisterDTO;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.model.User;
import com.powercess.printersystem.printersystem.service.UserService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseResult<User> register(@RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }
    @PostMapping("/login")
    public ResponseResult<LoginResponseDTO> login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }
    @GetMapping("/me")
    public ResponseResult<User> getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getUserById(userId);
        return ResponseResult.success(user);
    }
}
