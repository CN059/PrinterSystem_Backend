package com.powercess.printersystem.printersystem.service;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.LoginDTO;
import com.powercess.printersystem.printersystem.dto.LoginResponseDTO;
import com.powercess.printersystem.printersystem.dto.RegisterDTO;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.mapper.UserMapper;
import com.powercess.printersystem.printersystem.model.User;
import com.powercess.printersystem.printersystem.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public ResponseResult<User> register(RegisterDTO dto) {
        if (userMapper.selectByAccount(dto.getAccount()) != null) {
            return ResponseResult.error(400, "账号已存在");
        }
        User user = new User();
        user.setAccount(dto.getAccount());
        user.setUsername(dto.getUsername());
        user.setPassword(PasswordUtils.hashPassword(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        userMapper.insert(user);
        // 插入后重新查一次，确保获取到数据库生成的字段（如 id、created_at）
        User createdUser = userMapper.selectById(user.getId());
        // 过滤掉 password 字段
        createdUser.setPassword(null);
        return ResponseResult.success(createdUser);
    }
    public ResponseResult<LoginResponseDTO> login(LoginDTO dto) {
        User user = userMapper.selectByAccount(dto.getAccount());
        if (user == null || !PasswordUtils.checkPassword(dto.getPassword(), user.getPassword())) {
            return ResponseResult.error(401, "账号或密码错误");
        }
        StpUtil.login(user.getId());
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setToken(StpUtil.getTokenValue());
        responseDTO.setUserId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setRole(user.getRole());
        return ResponseResult.success(responseDTO);
    }
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }
}