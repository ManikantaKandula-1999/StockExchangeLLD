package com.example.stockexchangelld.services.strategies.impl;

import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.OrderStatus;
import com.example.stockexchangelld.services.strategies.OrderExpiryStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IntraDayExpiryStrategy implements OrderExpiryStrategy {
    public boolean checkExpiry(Order order){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderAcceptedTimeStamp = order.getOrderAcceptedTimestamp();
        if(now.isAfter(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30)) && orderAcceptedTimeStamp.isBefore(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 15, 30))){
            order.setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }
}
