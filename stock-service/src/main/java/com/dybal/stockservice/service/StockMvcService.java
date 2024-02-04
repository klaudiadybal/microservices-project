package com.dybal.stockservice.service;

import com.dybal.stockservice.model.Stock;
import com.dybal.stockservice.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockMvcService {

    private final StockRepository stockRepository;

    public List<Stock> getStocks() {
        return stockRepository.findAll();
    }

    public Stock getStock(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.orElseThrow(() ->  new IllegalArgumentException("Stock not found"));
    }

    public void createStock(Stock stock) {
        stockRepository.save(stock);
    }

    public void updateStock(Stock stock) {
        Optional<Stock> existingStock = stockRepository.findById(stock.getId());

        if (existingStock.isPresent()) {
            Stock existing = existingStock.get();
            existing.setProductName(stock.getProductName());
            existing.setQuantity(stock.getQuantity());
            stockRepository.save(existing);
        } else {
            throw new EntityNotFoundException(String.valueOf(stock.getId()));
        }
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
