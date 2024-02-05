package com.dybal.orderservice.service;

import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderMvcService {

    private final OrderRepository orderRepository;

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() ->  new IllegalArgumentException("Order not found"));
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    public void updateOrder(Order order) {
        Optional<Order> existingOrder = orderRepository.findById(order.getId());

        if (existingOrder.isPresent()) {
            Order existing = existingOrder.get();
            existing.setProducts(order.getProducts());
            orderRepository.save(existing);
        } else {
            throw new EntityNotFoundException(String.valueOf(order.getId()));
        }
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
