package com.dybal.productservice.controller;

import com.dybal.productservice.model.Product;
import com.dybal.productservice.service.ProductMvcService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductMvcController {

    private final ProductMvcService productService;

    @GetMapping("/list")
    public String getProducts (Model model){
        List<Product> products = productService.getProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/saveForm")
    public String showSaveForm(Model model){
        Product product = new Product();
        model.addAttribute("product", product);
        return "form";
    }

    @PostMapping("/save")
    public String saveOrUpdateProduct(@Valid @ModelAttribute("product") Product product, Errors errors) {
        if (!errors.hasErrors()) {
            if (product.getId() != null) {
                productService.updateProduct(product);
            } else {
                productService.createProduct(product);
            }
            return "redirect:/products/list";
        }
        return "form";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") Long id, Model model){
        Product product = productService.getProduct(id);

        model.addAttribute("product", product);
        return "form";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Long id){
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }

}
