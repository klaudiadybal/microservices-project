package com.dybal.orderservice.controller;

import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.service.OrderMvcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderMvcController {

    private final OrderMvcService orderService;

    @GetMapping("/list")
    public String getOrders (Model model){
        List<Order> orders = orderService.getOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/saveForm")
    public String showSaveForm(Model model){
        Order order = new Order();
        model.addAttribute("order", order);
        return "form";
    }

    @PostMapping("/save")
    public String saveOrUpdateOrder(@Valid @ModelAttribute("order") Order order, Errors errors) {
        if (!errors.hasErrors()) {
            if (order.getId() != null) {
                orderService.updateOrder(order);
            } else {
                orderService.createOrder(order);
            }
            return "redirect:/orders/list";
        }
        return "form";
    }

    @GetMapping("/updateForm")
    public String showUpdateForm(@RequestParam("id") Long id, Model model){
        Order order = orderService.getOrder(id);

        model.addAttribute("order", order);
        return "form";
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam("id") Long id){
        orderService.deleteOrder(id);
        return "redirect:/orders/list";
    }

}
