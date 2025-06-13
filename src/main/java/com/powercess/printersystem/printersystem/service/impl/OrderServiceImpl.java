package com.powercess.printersystem.printersystem.service.impl;

import com.powercess.printersystem.printersystem.exception.BusinessException;
import com.powercess.printersystem.printersystem.mapper.*;
import com.powercess.printersystem.printersystem.model.Address;
import com.powercess.printersystem.printersystem.model.PrintOrder;
import com.powercess.printersystem.printersystem.service.OrderService;
import com.powercess.printersystem.printersystem.utils.PageRangeUtils;
import com.powercess.printersystem.printersystem.utils.PrintPriceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DocumentMapper documentMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SystemStatisticsMapper systemStatisticsMapper;
    @Override
    public PrintOrder createOrder(PrintOrder order) {
        // 1. 生成订单号
        String orderNo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%06d", new Random().nextInt(999999));
        order.setOrderNo(orderNo);
        // 2. 设置默认状态和时间
        order.setStatus("UNPAID");
        order.setCreatedAt(LocalDateTime.now());
        // 3. 获取文档页数
        Integer pageCount = documentMapper.getPageCountById(order.getDocumentId());
        if (pageCount == null || pageCount <= 0) {
            throw new IllegalArgumentException("文档不存在或页数无效");
        }
        Integer addressId = order.getAddressId();
        if (addressId == null) {
            throw new BusinessException(400, "地址ID不能为空");
        }
        Address address = addressMapper.selectById(Long.valueOf(addressId));
        if (address == null) {
            throw new BusinessException(400, "地址不存在");
        }
        // 4. 判断打印范围，计算实际页数
        int actualPages;
        if ("ALL".equals(order.getPrintRange())) {
            actualPages = pageCount;
        } else if ("CUSTOM".equals(order.getPrintRange())) {
            actualPages = PageRangeUtils.parseCustomPageRange(order.getPrintRange());
        } else {
            throw new IllegalArgumentException("无效的打印范围");
        }
        // 5. 计算实际打印张数（用于价格）
        boolean duplex = order.getDuplex();
        int printSheets = PrintPriceCalculator.calculatePrintSheets(actualPages, duplex);
        // 6. 获取用户等级（直接通过 UserMapper 查询）
        Integer userLevel = userMapper.getUserLevelById(order.getUserId());
        if (userLevel == null || userLevel < 1 || userLevel > 6) {
            userLevel = 1; // 默认为 lv1
        }
        // 7. 计算价格
        BigDecimal price = PrintPriceCalculator.calculatePrice(printSheets, duplex, userLevel);
        // 8. 设置订单属性
        order.setTotalPages(actualPages); // 实际打印页数
        order.setPrice(price); // 最终价格
        order.setCopies(order.getCopies() != null ? order.getCopies() : 1); // 默认为1份
        // 9. 插入数据库
        orderMapper.insertOrder(order);
        // 10. 返回完整订单信息（从数据库重新查询）
        return orderMapper.selectById(order.getId());
    }
    @Override
    public PrintOrder getOrderById(Integer id) {
        return orderMapper.selectById(id);
    }
    @Override
    public List<PrintOrder> getOrdersByUserId(Integer userId) {
        return orderMapper.selectByUserId(userId);
    }
    @Transactional
    @Override
    public boolean payOrder(Integer orderId) {
        PrintOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getStatus().equals("UNPAID")) {
            return false;
        }
        orderMapper.updateOrderToPaid(orderId);
        // 更新统计表
        LocalDateTime now = LocalDateTime.now();
        systemStatisticsMapper.incrementDailyStats(
                now.toLocalDate(), order.getTotalPages(), order.getPrice().intValue()
        );
        return true;
    }
    @Override
    public boolean cancelOrder(Integer orderId) {
        PrintOrder order = orderMapper.selectById(orderId);
        if (order == null || !order.getStatus().equals("UNPAID")) {
            return false;
        }
        return orderMapper.deleteOrderById(orderId) > 0;
    }
    @Override
    public List<PrintOrder> getAllOrders() {
        return orderMapper.selectAllOrders();
    }
    @Override
    public boolean updateOrderStatus(Integer id, String newStatus, String shippingRemark) {
        return orderMapper.updateOrderStatus(id, newStatus, shippingRemark) > 0;
    }
    @Override
    public boolean deleteOrder(Integer id) {
        return orderMapper.deleteOrderById(id) > 0;
    }
}