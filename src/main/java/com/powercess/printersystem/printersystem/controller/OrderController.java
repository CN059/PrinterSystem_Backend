package com.powercess.printersystem.printersystem.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.powercess.printersystem.printersystem.dto.ResponseResult;
import com.powercess.printersystem.printersystem.model.PrintOrder;
import com.powercess.printersystem.printersystem.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    // 创建订单
    @PostMapping("/create")
    public ResponseResult<PrintOrder> createOrder(@RequestBody PrintOrder order) {
        order.setUserId(StpUtil.getLoginIdAsInt());
        PrintOrder created = orderService.createOrder(order);
        return ResponseResult.success(created);
    }
    // 获取订单列表（分页）
    @GetMapping("/list")
    public ResponseResult<PaginatedData<PrintOrder>> listOrders(
            @RequestParam(name = "page", defaultValue = "1") String pageStr,
            @RequestParam(name = "size", defaultValue = "10") String sizeStr) {
        int page = Integer.parseInt(pageStr);
        int size = Integer.parseInt(sizeStr);
        List<PrintOrder> orders = orderService.getOrdersByUserId(StpUtil.getLoginIdAsInt());
        int total = orders.size();
        return ResponseResult.success(new PaginatedData<>(orders, total));
    }
    // 获取订单详情
    @GetMapping("/detail/{orderId}")
    public ResponseResult<PrintOrder> detail(@PathVariable Integer orderId) {
        PrintOrder order = orderService.getOrderById(orderId);
        if (order == null || order.getUserId() != StpUtil.getLoginIdAsInt()) {
            throw new IllegalArgumentException("订单不存在或无权限");
        }
        return ResponseResult.success(order);
    }
    // 支付订单
    @PostMapping("/pay/{orderId}")
    public ResponseResult<ExpChange> payOrder(@PathVariable Integer orderId) {
        if (!orderService.payOrder(orderId)) {
            throw new IllegalArgumentException("支付失败");
        }
        return ResponseResult.success(new ExpChange(40)); // 示例经验值变化
    }
    // 取消订单
    @DeleteMapping("/cancel/{orderId}")
    public ResponseResult<Void> cancelOrder(@PathVariable Integer orderId) {
        if (!orderService.cancelOrder(orderId)) {
            throw new IllegalArgumentException("取消失败");
        }
        return ResponseResult.success(null);
    }
    // 管理员相关接口
    @GetMapping("/admin/orders")
    public ResponseResult<PaginatedData<PrintOrder>> adminListOrders(
            @RequestParam(name = "page", defaultValue = "1") String pageStr,
            @RequestParam(name = "size", defaultValue = "10") String sizeStr) {
        if (!StpUtil.hasRole("ADMIN")) {
            throw new SecurityException("无管理员权限");
        }
        int page = Integer.parseInt(pageStr);
        int size = Integer.parseInt(sizeStr);
        List<PrintOrder> orders = orderService.getAllOrders();
        int total = orders.size();
        return ResponseResult.success(new PaginatedData<>(orders, total));
    }
    @PatchMapping("/admin/order/status")
    public ResponseResult<Void> updateStatus(@RequestBody UpdateStatusRequest request) {
        if (!StpUtil.hasRole("ADMIN")) {
            throw new SecurityException("无管理员权限");
        }
        if (!orderService.updateOrderStatus(request.getOrderId(), request.getNewStatus(), request.getShippingRemark())) {
            throw new IllegalArgumentException("更新失败");
        }
        return ResponseResult.success(null);
    }
    @DeleteMapping("/admin/order/delete/{orderId}")
    public ResponseResult<Void> deleteOrder(@PathVariable Integer orderId) {
        if (!StpUtil.hasRole("ADMIN")) {
            throw new SecurityException("无管理员权限");
        }
        if (!orderService.deleteOrder(orderId)) {
            throw new IllegalArgumentException("删除失败");
        }
        return ResponseResult.success(null);
    }
    // 内部类：用于包装分页数据
    private static class PaginatedData<T> {
        private final List<T> list;
        private final int total;
        public PaginatedData(List<T> list, int total) {
            this.list = list;
            this.total = total;
        }
        public List<T> getList() { return list; }
        public int getTotal() { return total; }
    }
    // 内部类：用于支付返回
    private static class ExpChange {
        private final int expChange;
        public ExpChange(int expChange) {
            this.expChange = expChange;
        }
        public int getExpChange() { return expChange; }
    }
    // 内部类：用于状态更新请求
    private static class UpdateStatusRequest {
        private Integer orderId;
        private String newStatus;
        private String shippingRemark;
        public Integer getOrderId() { return orderId; }
        public String getNewStatus() { return newStatus; }
        public String getShippingRemark() { return shippingRemark; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }
        public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
        public void setShippingRemark(String shippingRemark) { this.shippingRemark = shippingRemark; }
    }
}