package com.example.pymesystem_backend.service;

import com.example.pymesystem_backend.dto.BillDTO;
import com.example.pymesystem_backend.model.Bill;

import java.util.List;

public interface BillService {
    BillDTO createBill(Bill bill);
    BillDTO getBillById(Long id);
    List<BillDTO> getAllBills();
    BillDTO updateBill(Long id, Bill bill);
    void deleteBill(Long id);
}
