package com.powercess.printersystem.printersystem.service;

import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.model.Address;
import java.util.List;
public interface AddressService {
    ResponseResult<List<Address>> getAddresses();
    ResponseResult<Address> addAddress(String fullAddress, String receiverName, String receiverPhone, Boolean isDefault);
    ResponseResult<?> setDefaultAddress(Long addressId);
}