package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class SaleProductDTO {

    private Long saleId;
    private Long productId;
    private int quantity;
    private double price;
}

