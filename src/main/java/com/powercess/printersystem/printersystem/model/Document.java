package com.powercess.printersystem.printersystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Document {
    private Long id;
    private Long userId;
    private String fileName;
    private String filePath;
    private Integer pageCount;
    private LocalDateTime uploadedAt;
    private Boolean isPrinted;
    private Boolean isDeleted;
}