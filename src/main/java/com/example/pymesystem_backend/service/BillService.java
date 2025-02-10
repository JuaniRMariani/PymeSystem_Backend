package com.example.pymesystem_backend.service;

import com.example.pymesystem_backend.dto.BillDTO;

import java.util.List;

public interface BillService {
    BillDTO createBill(BillDTO bill);
    BillDTO getBillById(Long id);
    List<BillDTO> getAllBills();
    BillDTO updateBill(Long id, BillDTO bill);
    void deleteBill(Long id);
}
