package com.dybal.productservice.dto;

import com.dybal.productservice.model.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotEmpty(message = "Enter product's name.")
    private String name;

    @NotEmpty(message = "Enter product's description.")
    private String description;

    @NotNull(message = "Enter product's price.")
    @Positive(message = "Price must be valid.")
    private BigDecimal price;

    public static Product convertRequestDtoToProduct(ProductRequest productDto){
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .build();
    }

}
