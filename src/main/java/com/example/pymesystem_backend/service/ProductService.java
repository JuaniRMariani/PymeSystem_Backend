package com.example.pymesystem_backend.service;

import com.example.pymesystem_backend.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO product);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
    ProductDTO updateProduct(Long id, ProductDTO product);
    void deleteProduct(Long id);
}
