package com.dybal.stockservice.dto;

import com.dybal.stockservice.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequest {

    private String productName;
    private Long quantity;


    public static Stock convertStockRequestDtoToStock(StockRequest stockRequest) {
        return Stock
                .builder()
                .productName(stockRequest.getProductName())
                .quantity(stockRequest.getQuantity())
                .build();
    }
}
