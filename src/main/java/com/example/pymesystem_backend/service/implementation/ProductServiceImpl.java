package com.example.pymesystem_backend.service.implementation;

import com.example.pymesystem_backend.dto.ProductDTO;
import com.example.pymesystem_backend.exception.InvalidProductException;
import com.example.pymesystem_backend.exception.NullProductException;
import com.example.pymesystem_backend.model.Product;
import com.example.pymesystem_backend.repository.ProductRepository;
import com.example.pymesystem_backend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDTO createProduct(ProductDTO product) {
        if(product == null)
            throw new NullProductException("Product cannot be null");
        Product savedProduct = saveProduct(modelMapper.map(product, Product.class));
        return convertToProductDTO(savedProduct);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new InvalidProductException("Product not found"));
        return convertToProductDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<Product> productsList = productRepository.findAll();
        return mapListProductToProductDTO(productsList);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO product) {
        if(product == null) throw new NullProductException("Product cannot be null");

        Product productToUpdate = productRepository.findById(id).orElseThrow(
                () -> new InvalidProductException("Product not found"));
        updateProductValues(productToUpdate, product);
        Product updatedProduct = saveProduct(productToUpdate);

        return convertToProductDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new InvalidProductException("Product not found"));
        productRepository.delete(product);
    }

    private ProductDTO convertToProductDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private void updateProductValues(Product productToUpdate, ProductDTO updatedProduct) {
        productToUpdate.setProductName(updatedProduct.getProductName());
        productToUpdate.setStock(updatedProduct.getStock());
        productToUpdate.setPrice(updatedProduct.getPrice());
        productToUpdate.setDescription(updatedProduct.getDescription());
    }

    private List<ProductDTO> mapListProductToProductDTO(List<Product> products) {
        return products.stream()
                .map(this::convertToProductDTO)
                .toList();
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
