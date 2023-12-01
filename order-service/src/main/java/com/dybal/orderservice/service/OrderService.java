package com.dybal.orderservice.service;

import com.dybal.orderservice.dto.OrderRequest;
import com.dybal.orderservice.dto.OrderResponse;
import com.dybal.orderservice.dto.ProductResponse;
import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

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

    public OrderResponse createOrder(OrderRequest orderRequest) {
        List<String> products = orderRequest.getProducts();


        // chceck if products exists in product database
        products
                .stream()
                .forEach(name -> {
                    webClient.get()
                            .uri(uriBuilder ->
                                    uriBuilder
                                            .scheme("http")
                                            .host("localhost")
                                            .port(8080)
                                            .path("/api/products/name/{name}")
                                            .build(name))
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError, response ->
                                    Mono.error(new IllegalArgumentException(
                                            String.format("Product with name: %s do not exists in products' database.", name))))
                            .onStatus(HttpStatusCode::is5xxServerError, response ->
                                    Mono.error(new IllegalArgumentException("Error in product service")))
                            .bodyToMono(ProductResponse.class)
                            .block();
                });

        Order order = OrderRequest.convertOrderRequestDtoToOrder(orderRequest);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.convertOrderToOrderResponseDto(savedOrder);
    }
}
