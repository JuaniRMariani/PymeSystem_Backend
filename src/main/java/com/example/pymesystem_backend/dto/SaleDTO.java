package com.example.pymesystem_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Payment method is required")
    @Pattern(regexp = "^(CASH|CARD|TRANSFER)$", message = "Payment method must be CASH, CARD or TRANSFER")
    private String paymentMethod;

    private double totalPrice;
    
    private LocalDateTime saleDate;

    @NotEmpty(message = "Sale must contain at least one product")
    @Valid
    private List<SaleProductDTO> saleProducts;
}

