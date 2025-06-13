package com.powercess.printersystem.printersystem.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class PrintOrder {
    private Integer id;
    private String orderNo;
    private Integer userId;
    private Integer documentId;
    private Integer addressId;
    private Integer totalPages;
    private BigDecimal price;
    private String printRange;
    private Boolean duplex;
    private String orientation;
    private String binding;
    private Integer copies;
    private String remark;
    private String shippingRemark;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime completedAt;
    // 关联对象（用于详情展示）
    private User user;
    private Document document;
    private Address address;
}