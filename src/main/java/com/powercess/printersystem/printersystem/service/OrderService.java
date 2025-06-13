package com.powercess.printersystem.printersystem.service;

import com.powercess.printersystem.printersystem.model.PrintOrder;

import java.util.List;

public interface OrderService {
    PrintOrder createOrder(PrintOrder order);
    PrintOrder getOrderById(Integer id);
    List<PrintOrder> getOrdersByUserId(Integer userId);
    boolean payOrder(Integer orderId);
    boolean cancelOrder(Integer orderId);
    List<PrintOrder> getAllOrders();
    boolean updateOrderStatus(Integer id, String newStatus, String shippingRemark);
    boolean deleteOrder(Integer id);
}