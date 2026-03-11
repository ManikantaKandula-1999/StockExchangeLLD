package com.example.stockexchangelld.services;

import com.example.stockexchangelld.data.OrderBook;
import com.example.stockexchangelld.dto.orderRequestDTO;
import com.example.stockexchangelld.exceptions.UserNotFoundException;
import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.OrderStatus;
import com.example.stockexchangelld.models.Trade;
import com.example.stockexchangelld.services.strategies.OrderExpiryStrategy;
import com.example.stockexchangelld.services.strategies.OrderMatchingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderBook orderBook;
    private final OrderMatchingStrategy orderMatchingStrategy;
    private final OrderExpiryStrategy orderExpiryStrategy;
    private final TradeService tradeService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Override
    public Order placeOrder(orderRequestDTO orderRequestDTO) throws UserNotFoundException{
        if(orderRequestDTO.getUserId() == null){
            throw new UserNotFoundException("User not found");
        }
        Order order = Order.builder()
                .userId(orderRequestDTO.getUserId())
                .orderType(orderRequestDTO.getOrderType())
                .stockSymbol(orderRequestDTO.getStockSymbol())
                .quantity(orderRequestDTO.getQuantity())
                .price(orderRequestDTO.getPrice())
                .build();

        order.setOrderAcceptedTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setRemainingQuantity(order.getQuantity());

        orderBook.addOrder(order);

        CompletableFuture.runAsync(() ->{
            try {
                executeOrderMatch(order);
            } catch (Exception e) {
                log.error("Error executing order match", e);
            }
        }, executorService);

        return order;
    }

    private void executeOrderMatch(Order newOrder){
        String stockSymbol = newOrder.getStockSymbol();
        List<Order> existingOrders = orderBook.getOrders(stockSymbol);

        //removing expired orders

        existingOrders = existingOrders.stream()
                .filter(order -> !orderExpiryStrategy.checkExpiry(order))
                .toList();

        existingOrders = existingOrders.stream()
                .filter(order -> !order.getOrderId().equalsIgnoreCase(newOrder.getOrderId()))
                .toList();

        List<Trade> executedTrades = orderMatchingStrategy.matchOrders(newOrder, existingOrders);

        if(!executedTrades.isEmpty()){
            for(Trade trade : executedTrades){
                log.info("Trade executed: {}", trade);
                tradeService.saveTrade(trade);
            }
        }

    }
    @Override
    public List<Order> getOrderBook(String symbol){
        log.info("Getting order book for symbol: {}", symbol);
        return orderBook.getOrders(symbol);
    }
}
