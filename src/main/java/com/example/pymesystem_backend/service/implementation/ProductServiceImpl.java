package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.ProductDTO;
import com.example.pymesystem_backend.model.Product;
import com.example.pymesystem_backend.repository.ProductRepository;
import com.example.pymesystem_backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

//    @Override
//    public ProductDTO createProduct(ProductDTO productDTO) {
//        // Ignora el DTO y crea un producto manualmente
//        Product product = new Product();
//        product.setProductName("Producto de prueba");
//        product.setStock(10);
//        product.setPrice(100.0);
//        product.setDescription("Descripción de prueba");
//
//        Product savedProduct = productRepository.save(product);
//        return modelMapper.map(savedProduct, ProductDTO.class);
//    }
}
