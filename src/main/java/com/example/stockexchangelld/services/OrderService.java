package com.example.stockexchangelld.services;

import com.example.stockexchangelld.dto.orderRequestDTO;
import com.example.stockexchangelld.exceptions.UserNotFoundException;
import com.example.stockexchangelld.models.Order;

import java.util.List;


public interface OrderService {
    Order placeOrder(orderRequestDTO orderRequestDTO) throws UserNotFoundException;

    List<Order> getOrderBook(String symbol);
}
