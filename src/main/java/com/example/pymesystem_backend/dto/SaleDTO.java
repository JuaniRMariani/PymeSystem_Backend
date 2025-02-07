package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class SaleDTO {

    private String sale_id;

    private String user_id;

    private int totalPrice;

    private String sale_date;

    private String payment_method;

}
