package com.powercess.printersystem.printersystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.mapper.AddressMapper;
import com.powercess.printersystem.printersystem.model.Address;
import com.powercess.printersystem.printersystem.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;
    @Override
    public ResponseResult<List<Address>> getAddresses() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Address> addresses = addressMapper.selectByUserId(userId);
        return ResponseResult.success(addresses);
    }
    @Override
    public ResponseResult<Address> addAddress(String fullAddress, String receiverName, String receiverPhone, Boolean isDefault) {
        Long userId = StpUtil.getLoginIdAsLong();
        Address address = new Address();
        address.setUserId(userId);
        address.setFullAddress(fullAddress);
        address.setReceiverName(receiverName);
        address.setReceiverPhone(receiverPhone);
        address.setIsDefault(isDefault != null && isDefault);
        if (address.getIsDefault()) {
            addressMapper.setAllNotDefault(userId);
        }
        addressMapper.insert(address);
        // 插入后重新查询，确保获取到数据库自动生成的字段（如 id、created_at）
        Address insertedAddress = addressMapper.selectById(address.getId());
        return ResponseResult.success(insertedAddress);
    }
    @Override
    public ResponseResult<?> setDefaultAddress(Long addressId) {
        Long userId = StpUtil.getLoginIdAsLong();
        Address address = addressMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return ResponseResult.error(403, "无权限操作该地址");
        }
        addressMapper.setAllNotDefault(userId);
        address.setIsDefault(true);
        addressMapper.update(address);
        return ResponseResult.success(null);
    }
}