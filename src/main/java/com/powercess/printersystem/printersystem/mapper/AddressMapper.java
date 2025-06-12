package com.powercess.printersystem.printersystem.mapper;

import com.powercess.printersystem.printersystem.model.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> selectByUserId(Long userId);
    Address selectById(Long id);
    void insert(Address address);
    void update(Address address);
    void setAllNotDefault(Long userId); // 将该用户所有地址设为非默认
}