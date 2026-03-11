package com.example.stockexchangelld.services.strategies.impl;

import com.example.stockexchangelld.models.Order;
import com.example.stockexchangelld.models.OrderStatus;
import com.example.stockexchangelld.models.OrderType;
import com.example.stockexchangelld.models.Trade;
import com.example.stockexchangelld.services.strategies.OrderMatchingStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FIFOMatchingStrategy implements OrderMatchingStrategy {

    @Override
    public String getStrategyName(){
        return "FIFO Matching Strategy";
    }

    @Override
    public List<Trade> matchOrders(Order newOrder, List<Order>existingOrders){
        OrderType orderType = newOrder.getOrderType();
        if(orderType == OrderType.BUY){
            return matchBuyOrders(newOrder, existingOrders);
        }else{
            return matchSellOrders(newOrder, existingOrders);
        }
    }

    private List<Trade> matchBuyOrders(Order buyOrder, List<Order> existingOrders){
        List<Trade> trades = new ArrayList<>();
        List<Order> matchingSellOrders =  existingOrders.stream()
                .filter(order -> order.getOrderType() == OrderType.SELL)
                .filter(order -> order.getStockSymbol().equalsIgnoreCase(buyOrder.getStockSymbol()))
                .filter(order -> order.getPrice() <= buyOrder.getPrice())
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderAcceptedTimestamp))
                .toList();

        int remainingQuantity = buyOrder.getQuantity();

        for(Order sellOrder : matchingSellOrders){
            if(remainingQuantity <=0){
                break;
            }

            int tradeQuantity = Math.min(remainingQuantity, sellOrder.getRemainingQuantity());

            Double tradePrice = sellOrder.getPrice();

            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(buyOrder.getStockSymbol())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();

            trades.add(trade);

            remainingQuantity = remainingQuantity - tradeQuantity;

            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity()- tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);
        }
        return trades;
    }

    private List<Trade> matchSellOrders(Order sellOrder, List<Order> existingOrders){
        List<Trade> trades = new ArrayList<>();

        List<Order> matchedBuyOrders = existingOrders.stream()
                .filter(order -> order.getOrderType() == OrderType.BUY)
                .filter(order -> order.getStockSymbol().equals(sellOrder.getStockSymbol()))
                .filter(order -> order.getPrice() >= sellOrder.getPrice())
                .filter(order -> order.getOrderStatus() == OrderStatus.ACCEPTED)
                .sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderAcceptedTimestamp))
                .toList();

        int remainingQuantity = sellOrder.getQuantity();

        for(Order buyOrder : matchedBuyOrders){
            if(remainingQuantity <=0){
                break;
            }

            int tradeQuantity = Math.min(remainingQuantity, buyOrder.getRemainingQuantity());
            Double tradePrice = buyOrder.getPrice();

            Trade trade = Trade.builder()
                    .buyerOrderId(buyOrder.getOrderId())
                    .sellerOrderId(sellOrder.getOrderId())
                    .stockSymbol(buyOrder.getStockSymbol())
                    .quantity(tradeQuantity)
                    .price(tradePrice)
                    .build();

            trades.add(trade);

            remainingQuantity = remainingQuantity - tradeQuantity;

            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);

            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);

        }

        return trades;
    }
}
