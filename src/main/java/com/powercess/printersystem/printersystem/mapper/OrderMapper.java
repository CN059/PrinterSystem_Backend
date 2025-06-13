package com.powercess.printersystem.printersystem.mapper;

import com.powercess.printersystem.printersystem.model.PrintOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    int insertOrder(PrintOrder order);
    PrintOrder selectById(Integer id);
    List<PrintOrder> selectByUserId(@Param("userId") Integer userId);
    List<PrintOrder> selectAllOrders();
    int updateOrderStatus(@Param("id") Integer id, @Param("status") String status, @Param("shippingRemark") String shippingRemark);
    int updateOrderToPaid(@Param("id") Integer id);
    int deleteOrderById(Integer id);
}