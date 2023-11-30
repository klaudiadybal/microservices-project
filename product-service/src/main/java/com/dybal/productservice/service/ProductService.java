package com.dybal.productservice.service;

import com.dybal.productservice.dto.ProductResponse;
import com.dybal.productservice.model.Product;
import com.dybal.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::convertProductToResponseDto)
                .toList();
    }

    public ProductResponse getProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product
                .map(ProductResponse::convertProductToResponseDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Record with id: %d not found.", id)));
    }
}
