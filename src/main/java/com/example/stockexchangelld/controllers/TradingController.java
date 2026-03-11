package com.example.stockexchangelld.controllers;

import com.example.stockexchangelld.dto.orderRequestDTO;
import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trading")
@RequiredArgsConstructor
public class TradingController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody orderRequestDTO orderRequestDTO){
        Order order = orderService.placeOrder(orderRequestDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orderBook/{symbol}")
    public ResponseEntity<Order> getOrderBook(@PathVariable String symbol){
        Order order = orderService.getOrderBook(symbol);
        return ResponseEntity.ok(order);
    }
}
