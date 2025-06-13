package com.powercess.printersystem.printersystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDetailResponse {
    private Integer id;
    private String orderNo;
    private User user;
    private Document document;
    private Address address;
    private Integer totalPages;
    private Double price;
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
    @Data
    public static class User {
        private Integer id;
        private String username;
    }
    @Data
    public static class Document {
        private Integer id;
        private String fileName;
        private Integer pageCount;
    }
    @Data
    public static class Address {
        private Integer id;
        private String fullAddress;
        private String receiverName;
        private String receiverPhone;
    }
}