package com.dybal.orderservice.dto;

import com.dybal.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<String> products;

    public static Order convertOrderRequestDtoToOrder(OrderRequest orderRequest){
        return Order.builder()
                .products(orderRequest.getProducts())
                .build();
    }
}
