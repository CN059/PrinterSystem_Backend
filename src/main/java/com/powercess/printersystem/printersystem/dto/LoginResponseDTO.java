package com.powercess.printersystem.printersystem.dto;

import lombok.Data;
@Data
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String username;
    private String role;
}