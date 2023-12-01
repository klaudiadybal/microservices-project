package com.dybal.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends ErrorResponseDto{

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductResponse(String apiPath, HttpStatus httpStatus, String errorMessage, LocalDateTime errorTime) {
        super(apiPath, httpStatus, errorMessage, errorTime);
    }
}
