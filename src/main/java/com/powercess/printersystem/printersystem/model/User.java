package com.powercess.printersystem.printersystem.model;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    private Long id;
    private String account;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String role; // USER / ADMIN
    private String status; // ENABLED / DISABLED
    private Integer exp;
    private LocalDateTime createdAt;
}