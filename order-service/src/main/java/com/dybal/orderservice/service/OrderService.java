package com.dybal.orderservice.service;

import com.dybal.orderservice.dto.OrderResponse;
import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    public List<OrderResponse> getOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::convertOrderToOrderResponseDto)
                .toList();
    }

    public OrderResponse getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order
                .map(OrderResponse::convertOrderToOrderResponseDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Order with id: %d not found.", id)));
    }
}
