package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.SaleDTO;
import com.example.pymesystem_backend.dto.SaleProductDTO;
import com.example.pymesystem_backend.model.*;
import com.example.pymesystem_backend.repository.*;
import com.example.pymesystem_backend.service.SaleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {
    
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    
    public SaleServiceImpl(SaleRepository saleRepository, 
                          UserRepository userRepository,
                          ProductRepository productRepository,
                          ModelMapper modelMapper) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        User user = userRepository.findById(saleDTO.getUserId()).orElse(null);
            
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(saleDTO.getPaymentMethod());
        
        double totalPrice = 0.0;
        List<SaleProduct> saleProducts = new ArrayList<>();
        for (SaleProductDTO saleProductDTO : saleDTO.getSaleProducts()) {
            Product product = productRepository.findById(saleProductDTO.getProductId()).orElse(null);
            
            if (product != null) {
                product.setStock(product.getStock() - saleProductDTO.getQuantity());
                productRepository.save(product);
                
                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSale(sale);
                saleProduct.setProduct(product);
                saleProduct.setQuantity(saleProductDTO.getQuantity());
                saleProduct.setPrice(product.getPrice());
                totalPrice += (product.getPrice() * saleProductDTO.getQuantity());
                saleProducts.add(saleProduct);
            }
        }
        
        sale.setTotalPrice(totalPrice);
        sale.setSaleProducts(saleProducts);
        Sale savedSale = saleRepository.save(sale);
        
        return convertToDTO(savedSale);
    }

    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        return(convertToDTO(sale));
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id).orElse(null);
        if (existingSale == null) return null;
            
        for (SaleProduct oldSaleProduct : existingSale.getSaleProducts()) {
            Product product = oldSaleProduct.getProduct();
            product.setStock(product.getStock() + oldSaleProduct.getQuantity());
            productRepository.save(product);
        }
        
        existingSale.getSaleProducts().clear();
        
        User user = userRepository.findById(saleDTO.getUserId()).orElse(null);
        if (user != null) {
            existingSale.setUser(user);
            existingSale.setPaymentMethod(saleDTO.getPaymentMethod());
            
            double totalPrice = 0.0;
            for (SaleProductDTO saleProductDTO : saleDTO.getSaleProducts()) {
                Product product = productRepository.findById(saleProductDTO.getProductId()).orElse(null);
                
                if (product != null) {
                    product.setStock(product.getStock() - saleProductDTO.getQuantity());
                    productRepository.save(product);
                    
                    SaleProduct saleProduct = new SaleProduct();
                    saleProduct.setSale(existingSale);
                    saleProduct.setProduct(product);
                    saleProduct.setQuantity(saleProductDTO.getQuantity());
                    saleProduct.setPrice(product.getPrice());
                    totalPrice += (product.getPrice() * saleProductDTO.getQuantity());
                    existingSale.getSaleProducts().add(saleProduct);
                }
            }
            
            existingSale.setTotalPrice(totalPrice);
            return convertToDTO(saleRepository.save(existingSale));
        }
        return null;
    }

    @Override
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);
        if (sale != null) {
            for (SaleProduct saleProduct : sale.getSaleProducts()) {
                Product product = saleProduct.getProduct();
                product.setStock(product.getStock() + saleProduct.getQuantity());
                productRepository.save(product);
            }
            saleRepository.delete(sale);
        }
    }

    //FUNCION QUE TE DIGO QUE VEAS
    private SaleDTO convertToDTO(Sale sale) {
        return modelMapper.map(sale, SaleDTO.class);
    }
} 