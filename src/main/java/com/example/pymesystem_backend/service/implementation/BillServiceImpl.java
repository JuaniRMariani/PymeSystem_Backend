package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.BillDTO;
import com.example.pymesystem_backend.exception.InvalidBillException;
import com.example.pymesystem_backend.exception.InvalidSaleException;
import com.example.pymesystem_backend.exception.NullBillException;
import com.example.pymesystem_backend.model.Bill;
import com.example.pymesystem_backend.model.Sale;
import com.example.pymesystem_backend.repository.BillRepository;
import com.example.pymesystem_backend.repository.SaleRepository;
import com.example.pymesystem_backend.service.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;

    public BillServiceImpl(BillRepository billRepository, SaleRepository saleRepository, ModelMapper modelMapper) {
        this.billRepository = billRepository;
        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BillDTO createBill(BillDTO bill) {
        if(bill == null)
            throw new NullBillException("Bill cannot be null");

        Bill billSaved = saveBill(modelMapper.map(bill, Bill.class));
        return convertToBillDTO(billSaved);
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
    public BillDTO updateBill(Long id, BillDTO bill) {
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
        return modelMapper.map(bill, com.example.pymesystem_backend.dto.BillDTO.class);
    }

    private List<BillDTO> mapListBillToBillDTO(List<Bill> bills) {
        return bills.stream()
                .map(this::convertToBillDTO)
                .collect(Collectors.toList());
    }

    private void updateBillValues(Bill billToUpdate, BillDTO bill) {
        Sale sale = getSaleBill(bill.getSaleId());
        billToUpdate.setSale(sale);
        billToUpdate.setBillType(bill.getBillType());
        billToUpdate.setBillNumber(bill.getBillNumber());
        billToUpdate.setCae(bill.getCae());
    }

    private Bill saveBill(Bill bill) {
        return billRepository.save(bill);
    }

    private Sale getSaleBill(Long id) {
        return saleRepository.findById(id).orElseThrow(
                () -> new InvalidSaleException("Sale not found"));
    }
}
