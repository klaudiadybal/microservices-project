package com.dybal.productservice.service;

import com.dybal.productservice.model.Product;
import com.dybal.productservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductMvcService {

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() ->  new IllegalArgumentException("Product not found"));
    }

    public void createProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findById(product.getId());

        if (existingProduct.isPresent()) {
            Product existing = existingProduct.get();
            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setPrice(product.getPrice());
            productRepository.save(existing);
        } else {
            throw new EntityNotFoundException(String.valueOf(product.getId()));
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
