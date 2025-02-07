package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class SaleProductDTO {

    private String sale_id;

    private String product_id;

    private int quantity;

    private double price;

}
