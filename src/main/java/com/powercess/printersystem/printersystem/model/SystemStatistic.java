package com.powercess.printersystem.printersystem.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SystemStatistic {
    private Long id;
    private LocalDate date;
    private Integer totalOrders;
    private Integer totalPrintedPages;
    private Double totalIncome;
    private LocalDateTime updatedAt;
}