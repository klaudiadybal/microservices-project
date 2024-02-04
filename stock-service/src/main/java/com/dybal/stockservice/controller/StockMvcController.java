package com.dybal.stockservice.controller;

import com.dybal.stockservice.model.Stock;
import com.dybal.stockservice.service.StockMvcService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockMvcController {

    private final StockMvcService stockService;

    @GetMapping("/list")
    public String getStocks (Model model){
        List<Stock> stocks = stockService.getStocks();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    @GetMapping("/saveForm")
    public String showSaveForm(Model model){
        Stock stock = new Stock();
        model.addAttribute("stock", stock);
        return "form";
    }

    @PostMapping("/save")
    public String saveOrUpdateStock(@Valid @ModelAttribute("stock") Stock stock, Errors errors) {
        if (!errors.hasErrors()) {
            if (stock.getId() != null) {
                stockService.updateStock(stock);
            } else {
                stockService.createStock(stock);
            }
            return "redirect:/stocks/list";
        }
        return "form";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") Long id, Model model){
        Stock stock = stockService.getStock(id);

        model.addAttribute("stock", stock);
        return "form";
    }

    @GetMapping("/delete")
    public String deleteStock(@RequestParam("id") Long id){
        stockService.deleteStock(id);
        return "redirect:/stocks/list";
    }

}
