package com.dybal.orderservice.dto;


import com.dybal.orderservice.model.Order;
import com.dybal.orderservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private List<Product> products;

    public static OrderResponse convertOrderToOrderResponseDto (Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .products(order.getProducts())
                .build();
    }
}
