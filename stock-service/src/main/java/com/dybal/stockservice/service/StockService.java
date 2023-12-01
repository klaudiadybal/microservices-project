package com.dybal.stockservice.service;

import com.dybal.stockservice.dto.ProductResponse;
import com.dybal.stockservice.dto.StockRequest;
import com.dybal.stockservice.dto.StockResponse;
import com.dybal.stockservice.model.Stock;
import com.dybal.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional
public class StockService {

    private final StockRepository stockRepository;
    private final WebClient webClient;

    public List<StockResponse> getStocks() {
        return stockRepository.findAll()
                .stream()
                .map(StockResponse::convertStockToStockResponseDto)
                .toList();
    }

    public StockResponse getStock(Long id) {
        Optional<Stock> optionalStock = stockRepository.findById(id);
        if(optionalStock.isPresent()){
            return optionalStock
                    .map(StockResponse::convertStockToStockResponseDto)
                    .get();
        } else {
            throw new IllegalArgumentException(String.format("Record with id: %d not found.", id));
        }
    }

    public StockResponse getStockByName(String name) {
        Optional<Stock> stock = stockRepository.findByProductName(name);

        return stock
                .map(StockResponse::convertStockToStockResponseDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Record with name: %s not found.", name)));

    }

    public StockResponse createStock(StockRequest stockRequest) {
        String name = stockRequest.getProductName();
        Optional<Stock> optionalStock = stockRepository.findByProductName(name);

        if(optionalStock.isPresent()) {
            throw new IllegalArgumentException(String.format("Record with name: %s already exists.", name));
        }

        getProductFromProductService(name);

        Stock stock = StockRequest.convertStockRequestDtoToStock(stockRequest);
        Stock savedStock = stockRepository.save(stock);
        return StockResponse.convertStockToStockResponseDto(savedStock);
    }

    public StockResponse updateStock(Long id, StockRequest stockRequest) {
        String productName = stockRequest.getProductName();
        Optional<Stock> optionalStockByName = stockRepository.findByProductName(productName);

        if(optionalStockByName.isPresent() && !id.equals(optionalStockByName.get().getId())) {
            throw new IllegalArgumentException(String.format("Product with name: %s already exists.", productName));
        }
        getProductFromProductService(productName);

        Optional<Stock> optionalStock = stockRepository.findById(id);

        if(optionalStock.isPresent()) {
            Stock stock = optionalStock.get();
            stock.setProductName(stockRequest.getProductName());
            stock.setQuantity(stockRequest.getQuantity());
            Stock savedStock = stockRepository.save(stock);

            return StockResponse.convertStockToStockResponseDto(savedStock);

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

    private void getProductFromProductService(String name) {
        ProductResponse product = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8080)
                                .path("/api/products/name/{name}")
                                .build(name))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new IllegalArgumentException("Product not found")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new IllegalArgumentException("Error in product service")))
                .bodyToMono(ProductResponse.class)
                .block();

        if (product == null || product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException(String.format("Product with name: %s not found in product database", name));
        }
    }

}
