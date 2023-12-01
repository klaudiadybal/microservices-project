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
public class StockResponse {

    private Long id;
    private String productName;
    private Long quantity;

    public static StockResponse convertStockToStockResponseDto(Stock stock) {
        return StockResponse.builder().id(stock.getId())
                .productName(stock.getProductName())
                .quantity(stock.getQuantity())
                .build();
    }
}
