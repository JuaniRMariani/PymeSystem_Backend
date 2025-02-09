package com.example.pymesystem_backend.service;

import com.example.pymesystem_backend.dto.SaleDTO;
import java.util.List;

public interface SaleService {
    SaleDTO createSale(SaleDTO saleDTO);
    List<SaleDTO> getAllSales();
    SaleDTO getSaleById(Long id);
    SaleDTO updateSale(Long id, SaleDTO saleDTO);
    void deleteSale(Long id);
} 