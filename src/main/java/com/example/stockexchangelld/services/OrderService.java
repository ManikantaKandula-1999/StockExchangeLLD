package com.example.stockexchangelld.services;

import com.example.stockexchangelld.dto.orderRequestDTO;
import com.example.stockexchangelld.models.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    Order placeOrder(orderRequestDTO orderRequestDTO);

    Order getOrderBook(String symbol);
}
