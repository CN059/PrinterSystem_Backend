package com.powercess.printersystem.printersystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.mapper.UserMapper;
import com.powercess.printersystem.printersystem.model.User;
import com.powercess.printersystem.printersystem.service.UserService;
import com.powercess.printersystem.printersystem.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public ResponseResult<User> register(String account, String username, String password, String phone, String email) {
        if (userMapper.selectByAccount(account) != null) {
            return ResponseResult.error(400, "账号已存在");
        }
        User user = new User();
        user.setAccount(account);
        user.setUsername(username);
        user.setPassword(PasswordUtils.hashPassword(password));
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole("USER");
        user.setStatus("ENABLED");
        userMapper.insert(user);
        // 重新获取插入后的完整用户对象
        User insertedUser = userMapper.selectById(user.getId());
        // 排除密码字段
        insertedUser.setPassword(null);
        return ResponseResult.success(insertedUser);
    }
    @Override
    public ResponseResult<?> login(String account, String password) {
        User user = userMapper.selectByAccount(account);
        if (user == null || !PasswordUtils.checkPassword(password, user.getPassword())) {
            return ResponseResult.error(401, "账号或密码错误");
        }
        StpUtil.login(user.getId());
        Map<String, Object> data = new HashMap<>();
        data.put("token", StpUtil.getTokenValue());
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("role", user.getRole());
        return ResponseResult.success(data);
    }
    @Override
    public ResponseResult<User> getCurrentUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null); // 安全处理
        }
        return ResponseResult.success(user);
    }
    @Override
    public ResponseResult<User> updateUserInfo(String account, String username, String phone, String email) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (account != null && !account.equals(user.getAccount())) {
            if (userMapper.selectByAccount(account) != null) {
                return ResponseResult.error(400, "账号已存在");
            }
            user.setAccount(account);
        }
        if (username != null) user.setUsername(username);
        if (phone != null) user.setPhone(phone);
        if (email != null) user.setEmail(email);
        userMapper.update(user);
        user = userMapper.selectById(userId); // 重新查询以获取最新数据
        user.setPassword(null); // 安全处理
        return ResponseResult.success(user);
    }
    @Override
    public ResponseResult<?> changePassword(String oldPassword, String newPassword) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        if (!PasswordUtils.checkPassword(oldPassword, user.getPassword())) {
            return ResponseResult.error(400, "旧密码错误");
        }
        String hashedNewPassword = PasswordUtils.hashPassword(newPassword);
        userMapper.updatePasswordById(userId, hashedNewPassword);
        // 可选：添加日志验证是否执行成功
        System.out.println("密码已更新为：" + hashedNewPassword);
        return ResponseResult.success(null);
    }
    @Override
    public ResponseResult<?> getExpAndLevel() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userMapper.selectById(userId);
        int level = user.getExp() / 100 + 1;
        Map<String, Object> data = new HashMap<>();
        data.put("exp", user.getExp());
        data.put("level", level);
        return ResponseResult.success(data);
    }
}