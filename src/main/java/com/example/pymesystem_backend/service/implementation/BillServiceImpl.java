package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.BillDTO;
import com.example.pymesystem_backend.exception.InvalidBillException;
import com.example.pymesystem_backend.exception.NullBillException;
import com.example.pymesystem_backend.model.Bill;
import com.example.pymesystem_backend.repository.BillRepository;
import com.example.pymesystem_backend.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final ModelMapper modelMapper;

    public BillServiceImpl(BillRepository billRepository, ModelMapper modelMapper) {
        this.billRepository = billRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BillDTO createBill(Bill bill) {
        if(bill == null) throw new NullBillException("Bill cannot be null");
        billRepository.save(bill);
        return convertToBillDTO(bill);
    }

    @Override
    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(
                () -> new InvalidBillException("Bill not found"));
        return convertToBillDTO(bill);
    }

    @Override
    public List<BillDTO> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        return mapListBillToBillDTO(bills);
    }

    @Override
    public BillDTO updateBill(Long id, Bill bill) {
        if(bill == null) throw new NullBillException("Bill cannot be null");

        Bill billToUpdate = billRepository.findById(id).orElseThrow(
                () -> new InvalidBillException("Bill not found"));
        updateBillValues(billToUpdate, bill);
        Bill updatedBill = billRepository.save(billToUpdate);

        return convertToBillDTO(updatedBill);
    }

    @Override
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id).orElseThrow(
                () -> new InvalidBillException("Bill not found"));
        billRepository.delete(bill);
    }

    private BillDTO convertToBillDTO(Bill bill) {
        return modelMapper.map(bill, BillDTO.class);
    }

    private List<BillDTO> mapListBillToBillDTO(List<Bill> bills) {
        return bills.stream()
                .map(this::convertToBillDTO)
                .collect(Collectors.toList());
    }

    private void updateBillValues(Bill billToUpdate, Bill bill) {
        billToUpdate.setSale(bill.getSale());
        billToUpdate.setBillType(bill.getBillType());
        billToUpdate.setBillNumber(bill.getBillNumber());
        billToUpdate.setCae(bill.getCae());
    }
}
