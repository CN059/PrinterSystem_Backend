package com.powercess.printersystem.printersystem.model;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Address {
    private Long id;
    private Long userId;
    private String fullAddress;
    private String receiverName;
    private String receiverPhone;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}