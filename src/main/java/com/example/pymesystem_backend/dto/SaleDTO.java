package com.example.pymesystem_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SaleDTO {

    private Long saleId;
    private Long userId;
    private double totalPrice;
    private LocalDateTime saleDate;
    private String paymentMethod;
}

