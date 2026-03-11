package com.example.stockexchangelld.data;

import com.example.stockexchangelld.exceptions.InvalidOrderException;
import com.example.stockexchangelld.models.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Component
public class OrderBookImpl implements OrderBook{
    Map<String, List<Order>> orderBook = new ConcurrentHashMap<>();
    Map<String, ReadWriteLock> symbolLocks = new ConcurrentHashMap<>();

    public void addOrder(Order order){
        String stockSymbol = order.getStockSymbol();
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();
        try {
            orderBook.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(order);
            log.info("Order added to order book: {} - {} - {}", order.getOrderId(), order.getStockSymbol(), order.getOrderType());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeOrder(String orderId, String stockSymbol){
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();

        try{
            List<Order> orders = orderBook.get(stockSymbol);

            if(orders != null){
                boolean removed = orders.removeIf(order -> order.getOrderId().equals(orderId));
                if(removed){
                    log.info("Order removed from order book: {}-{}", orderId, stockSymbol);
                    return true;
                }else{
                    log.info("Order not found in order book: {}-{}", orderId, stockSymbol);
                }
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }

    }
    public boolean updateOrder(Order updatedOrder){
        String stockSymbol = updatedOrder.getStockSymbol();
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.writeLock().lock();

        try{
            List<Order> orders = orderBook.get(stockSymbol);
            if (orders != null){
                for(int i = 0; i < orders.size(); i++){
                    if(orders.get(i).getOrderId().equals(updatedOrder.getOrderId())){
                        orders.set(i, updatedOrder);
                        log.info("Order updated in order book: {}-{}", updatedOrder.getOrderId(), stockSymbol);
                        return true;
                    }
                }
            }
            return false;
        }finally {
            lock.writeLock().unlock();
        }
    }
    public List<Order> getOrders(String stockSymbol){
        log.info("Getting order book for symbol: {}", stockSymbol);
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.readLock().lock();
        try{
            log.info("Order book size: {}", orderBook);
            return orderBook.getOrDefault(stockSymbol, new ArrayList<>());
        }finally {
            lock.readLock().unlock();
        }
    }
    public Optional<Order> getOrderById(String orderId) throws InvalidOrderException {
        if(orderId == null){
            throw new InvalidOrderException("Order ID cannot be null");
        }
        for(Map.Entry<String, List<Order>> entry : orderBook.entrySet()){
            String stockSymbol = entry.getKey();
            ReadWriteLock lock = getOrCreateLock(stockSymbol);
            lock.readLock().lock();
            try{
                List<Order> orders = entry.getValue();
                for(Order order : orders){
                    if(order.getOrderId().equals(orderId)){
                        return Optional.of(order);
                    }
                }
            }
            finally {
                lock.readLock().unlock();
            }
        }
        return Optional.empty();
    }
    public Optional<Order> getOrderBySymbol(String stockSymbol){
        ReadWriteLock lock = getOrCreateLock(stockSymbol);
        lock.readLock().lock();
        try{
            return Optional.ofNullable(orderBook.get(stockSymbol).stream().filter(order -> order.getStockSymbol().equals(stockSymbol)).findFirst().orElse(null));
        } finally {
            lock.readLock().unlock();
        }
    }

    private ReadWriteLock getOrCreateLock(String stockSymbol){
        return symbolLocks.computeIfAbsent(stockSymbol, k -> new ReentrantReadWriteLock());
    }
}
