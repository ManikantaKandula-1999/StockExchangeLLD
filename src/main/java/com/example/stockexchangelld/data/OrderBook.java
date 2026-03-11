package com.example.stockexchangelld.data;

import com.example.stockexchangelld.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderBook {
    void addOrder(Order order);
    boolean removeOrder(String orderId, String symbol);
    boolean updateOrder(Order updatedOrder);
    List<Order> getOrders(String stockSymbol);
    Optional<Order> getOrderById(String orderId);
    Optional<Order> getOrderBySymbol(String stockSymbol);
}
