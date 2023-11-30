package com.dybal.stockservice.service;

import com.dybal.stockservice.dto.StockRequest;
import com.dybal.stockservice.dto.StockResponse;
import com.dybal.stockservice.model.Stock;
import com.dybal.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<StockResponse> getStocks() {
        return stockRepository.findAll()
                .stream()
                .map(StockResponse::convertFromStockToStockResponseDto)
                .toList();
    }

    public StockResponse getStock(Long id) {
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if(optionalStock.isPresent()){
            return optionalStock
                    .map(StockResponse::convertFromStockToStockResponseDto)
                    .get();
        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }

    }

    public StockResponse createStock(StockRequest stockRequest) {
        String name = stockRequest.getProductName();
        Optional<Stock> optionalStock = stockRepository.findByProductName(name);

        if(optionalStock.isPresent()) {
            throw new IllegalArgumentException(String.format("Record with name: %s already exists.", name));
        } else {
            Stock stock = StockRequest.convertStockRequestDtoToStock(stockRequest);
            Stock savedStock = stockRepository.save(stock);
            return StockResponse.convertFromStockToStockResponseDto(savedStock);
        }

    }

    public StockResponse updateStock(Long id, StockRequest stockRequest) {
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if(optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            stock.setProductName(stockRequest.getProductName());
            stock.setQuantity(stockRequest.getQuantity());
            Stock savedStock = stockRepository.save(stock);

            return StockResponse.convertFromStockToStockResponseDto(savedStock);

        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }

    }

    public void deleteStock(Long id) {
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if(optionalStock.isPresent()) {
            stockRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }
    }
}
