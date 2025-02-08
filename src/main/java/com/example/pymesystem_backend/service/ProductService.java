package com.example.pymesystem_backend.service;

import com.example.pymesystem_backend.dto.ProductDTO;
import com.example.pymesystem_backend.model.Product;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(Product product);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
