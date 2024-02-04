package com.dybal.stockservice.controller;

import com.dybal.stockservice.dto.StockRequest;
import com.dybal.stockservice.dto.StockResponse;
import com.dybal.stockservice.service.StockRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockRestController {

    private final StockRestService stockService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponse> getStocks() {
        return stockService.getStocks();
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponse getStock(@PathVariable("id") Long id) {
        return stockService.getStock(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponse getStockByName(@PathVariable("name") String name){
        return stockService.getStockByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StockResponse createStock(@RequestBody StockRequest stockRequest){
        return stockService.createStock(stockRequest);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StockResponse updateStock(@PathVariable("id") Long id, @RequestBody StockRequest stockRequest){
        return stockService.updateStock(id, stockRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStock(@PathVariable("id") Long id) {
        stockService.deleteStock(id);
    }


}
