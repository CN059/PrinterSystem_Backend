package com.powercess.printersystem.printersystem.dto;

import lombok.Data;
@Data
public class RegisterDTO {
    private String account;
    private String username;
    private String password;
    private String phone;
    private String email;
}