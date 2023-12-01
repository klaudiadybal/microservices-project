package com.dybal.productservice.service;

import com.dybal.productservice.dto.ProductRequest;
import com.dybal.productservice.dto.ProductResponse;
import com.dybal.productservice.model.Product;
import com.dybal.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public ProductResponse getProductByName(String name) {
        Optional<Product> product = productRepository.findByName(name);

            return product
                    .map(ProductResponse::convertProductToResponseDto)
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Record with name: %s not found.", name)));

    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        String name = productRequest.getName();
        if(productRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException(String.format("Record with name: %s already exists.", name));
        } else {
            Product product = ProductRequest.convertRequestDtoToProduct(productRequest);
            Product savedProduct = productRepository.save(product);
            return ProductResponse.convertProductToResponseDto(savedProduct);
        }
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        String name = productRequest.getName();
        Optional<Product> optionalProductByName = productRepository.findByName(name);

        if(optionalProductByName.isPresent() && !id.equals(optionalProductByName.get().getId())) {
            throw new IllegalArgumentException((String.format("Product with name: %s already exists.", name)));
        }

        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(productRequest.getPrice());
            productRepository.save(product);

            return ProductResponse.convertProductToResponseDto(product);

        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }
    }

    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }

    }


}
