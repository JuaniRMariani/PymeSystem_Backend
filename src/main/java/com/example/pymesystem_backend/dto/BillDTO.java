package com.example.pymesystem_backend.dto;

import lombok.Data;

@Data
public class BillDTO {

    private Long billId;
    private Long saleId;
    private String billType;
    private String billNumber;
    private String cae;
}
