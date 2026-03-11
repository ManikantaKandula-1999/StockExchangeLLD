package com.example.stockexchangelld.controllers;

import com.example.stockexchangelld.dto.orderRequestDTO;
import com.example.stockexchangelld.exceptions.UserNotFoundException;
import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trading")
@RequiredArgsConstructor
public class TradingController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(@RequestBody orderRequestDTO orderRequestDTO) throws UserNotFoundException {
        Order order = orderService.placeOrder(orderRequestDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orderBook/{symbol}")
    public ResponseEntity<List<Order>> getOrderBook(@PathVariable String symbol){
        List<Order> orderBook = orderService.getOrderBook(symbol);
        return ResponseEntity.ok(orderBook);
    }
}
