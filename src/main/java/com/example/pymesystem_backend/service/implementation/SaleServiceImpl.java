package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.SaleDTO;
import com.example.pymesystem_backend.dto.SaleProductDTO;
import com.example.pymesystem_backend.exception.InsufficientStockException;
import com.example.pymesystem_backend.exception.InvalidOperationException;
import com.example.pymesystem_backend.exception.ResourceNotFoundException;
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
        if (saleDTO == null) {
            throw new InvalidOperationException("Sale data cannot be null");
        }
        
        if (saleDTO.getSaleProducts() == null || saleDTO.getSaleProducts().isEmpty()) {
            throw new InvalidOperationException("Sale must contain at least one product");
        }
        
        try {
            Sale sale = initializeSale(saleDTO);
            List<SaleProduct> saleProducts = processSaleProducts(sale, saleDTO.getSaleProducts());
            
            sale.setSaleProducts(saleProducts);
            sale.setTotalPrice(calculateTotalPrice(saleProducts));
            
            Sale savedSale = saleRepository.save(sale);
            return convertToDTO(savedSale);
        } catch (Exception e) {
            throw new InvalidOperationException("Error creating sale: " + e.getMessage());
        }
    }


    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return convertToDTO(sale);
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        if (saleDTO == null) {
            throw new InvalidOperationException("Sale data cannot be null");
        }
        
        Sale existingSale = saleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        
        try {
            updateSaleValues(existingSale, saleDTO);
            return convertToDTO(saleRepository.save(existingSale));
        } catch (Exception e) {
            throw new InvalidOperationException("Error updating sale: " + e.getMessage());
        }
    }

    @Override
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        
        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            Product product = saleProduct.getProduct();
            product.setStock(product.getStock() + saleProduct.getQuantity());
            productRepository.save(product);
        }
        saleRepository.delete(sale);
    }

    private Sale initializeSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        User user = userRepository.findById(saleDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + saleDTO.getUserId()));
        
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(saleDTO.getPaymentMethod());
        
        return sale;
    }

    private List<SaleProduct> processSaleProducts(Sale sale, List<SaleProductDTO> saleProductDTOs) {
        List<SaleProduct> saleProducts = new ArrayList<>();
        
        for (SaleProductDTO saleProductDTO : saleProductDTOs) {
            Product product = productRepository.findById(saleProductDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + saleProductDTO.getProductId()));
            
            if (product.getStock() < saleProductDTO.getQuantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getProductName() + 
                    ". Available: " + product.getStock() + 
                    ", Requested: " + saleProductDTO.getQuantity()
                );
            }
            
            updateProductStock(product, saleProductDTO.getQuantity());
            saleProducts.add(createSaleProduct(sale, product, saleProductDTO.getQuantity()));
        }
        
        return saleProducts;
    }

    private void updateProductStock(Product product, int quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    private SaleProduct createSaleProduct(Sale sale, Product product, int quantity) {
        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setSale(sale);
        saleProduct.setProduct(product);
        saleProduct.setQuantity(quantity);
        saleProduct.setPrice(product.getPrice());
        return saleProduct;
    }

    private double calculateTotalPrice(List<SaleProduct> saleProducts) {
        return saleProducts.stream()
                .mapToDouble(sp -> sp.getPrice() * sp.getQuantity())
                .sum();
    }

    private void updateSaleValues(Sale existingSale, SaleDTO saleDTO) {
        User user = userRepository.findById(saleDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + saleDTO.getUserId()));
        
        for (SaleProduct oldSaleProduct : existingSale.getSaleProducts()) {
            Product product = oldSaleProduct.getProduct();
            product.setStock(product.getStock() + oldSaleProduct.getQuantity());
            productRepository.save(product);
        }
        
        existingSale.getSaleProducts().clear();
        existingSale.setUser(user);
        existingSale.setPaymentMethod(saleDTO.getPaymentMethod());
        
        double totalPrice = 0.0;
        for (SaleProductDTO saleProductDTO : saleDTO.getSaleProducts()) {
            Product product = productRepository.findById(saleProductDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + saleProductDTO.getProductId()));
            
            if (product.getStock() < saleProductDTO.getQuantity()) {
                throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getProductName() + 
                    ". Available: " + product.getStock() + 
                    ", Requested: " + saleProductDTO.getQuantity()
                );
            }
            
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
        
        existingSale.setTotalPrice(totalPrice);
    }

    private SaleDTO convertToDTO(Sale sale) {
        return modelMapper.map(sale, SaleDTO.class);
    }
} 