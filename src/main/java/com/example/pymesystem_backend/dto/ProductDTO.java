package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String productName;
    private int stock;
    private double price;
    private String description;
}
