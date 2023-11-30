package com.dybal.productservice.service;

import com.dybal.productservice.dto.ProductResponse;
import com.dybal.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
