package com.dybal.orderservice.service;

import com.dybal.orderservice.dto.OrderRequest;
import com.dybal.orderservice.dto.OrderResponse;
import com.dybal.orderservice.dto.ProductResponse;
import com.dybal.orderservice.dto.StockResponse;
import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderRestService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @PersistenceContext
    private EntityManager entityManager;

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

        checkDoProductsExist(products);

        Optional<String> outOfStockProduct = findOutOfStockProduct(products);
        boolean areInStock = outOfStockProduct.isEmpty();

        if(areInStock) {
            Order order = OrderRequest.convertOrderRequestDtoToOrder(orderRequest);
            Order savedOrder = orderRepository.save(order);
            return OrderResponse.convertOrderToOrderResponseDto(savedOrder);
        } else {
            throw new IllegalArgumentException(String.format("Product with name: %s is out of stock.", outOfStockProduct.get()));
        }
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format("Product with id: %d not found.", id));
        }

        Order order = orderOptional.get();
        List<String> products = orderRequest.getProducts();
        checkDoProductsExist(products);
        Optional<String> outOfStockProduct = findOutOfStockProduct(products);
        boolean areInStock = outOfStockProduct.isEmpty();

        if(areInStock){
            order.setProducts(orderRequest.getProducts());
            Order savedOrder = orderRepository.save(order);
            return OrderResponse.convertOrderToOrderResponseDto(savedOrder);
        } else {
            throw new IllegalArgumentException(String.format("Product with name: %s is out of stock.", outOfStockProduct.get()));
        }

    }

    @Transactional
    public void deleteOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order order = optionalOrder
                .orElseThrow(() -> new IllegalArgumentException(String.format("Product with id: %d not found.", id)));

        entityManager.createNativeQuery("DELETE FROM ORDERS_PRODUCTS WHERE ORDER_ID = :orderId")
                .setParameter("orderId", id)
                .executeUpdate();
        orderRepository.deleteById(id);
    }


    private void checkDoProductsExist(List<String> products) {
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
                                            String.format("Product with name: %s do not exist in products' database.", name))))
                            .onStatus(HttpStatusCode::is5xxServerError, response ->
                                    Mono.error(new IllegalArgumentException("Error in product service")))
                            .bodyToMono(ProductResponse.class)
                            .block();
                });
    }

    private Optional<String> findOutOfStockProduct(List<String> products) {
        return products
                .stream()
                .filter(name -> {
                    StockResponse stockResponse = webClient.get()
                            .uri(uriBuilder ->
                                    uriBuilder
                                            .scheme("http")
                                            .host("localhost")
                                            .port(8081)
                                            .path("/api/stocks/name/{name}")
                                            .build(name))
                            .retrieve()
                            .onStatus(HttpStatusCode::is4xxClientError, response ->
                                    Mono.error(new IllegalArgumentException(
                                            String.format("Product with name: %s do not exist in stocks' database.", name))))
                            .onStatus(HttpStatusCode::is5xxServerError, response ->
                                    Mono.error(new IllegalArgumentException("Error in product service")))
                            .bodyToMono(StockResponse.class)
                            .block();
                    return stockResponse != null && stockResponse.getQuantity() <= 0;
                })
                .findFirst();
    }

}
