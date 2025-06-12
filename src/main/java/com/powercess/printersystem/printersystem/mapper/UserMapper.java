package com.powercess.printersystem.printersystem.mapper;

import com.powercess.printersystem.printersystem.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(Long id);
    User selectByAccount(String account);
    void insert(User user);
    void update(User user);
}