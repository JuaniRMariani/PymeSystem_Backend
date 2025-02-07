package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String product_id;

    private String product_name;

    private int Stock;

    private double price;

    private String description;

}
