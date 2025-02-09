package com.example.pymesystem_backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDTO {

    private Long id;
    private Long userId;
    private double totalPrice;
    private LocalDateTime saleDate;
    private String paymentMethod;
    private List<SaleProductDTO> saleProducts;
}

