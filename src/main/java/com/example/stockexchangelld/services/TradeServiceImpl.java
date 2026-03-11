package com.example.stockexchangelld.services;

import com.example.stockexchangelld.models.Trade;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TradeServiceImpl implements TradeService {
    Map<String, Trade> tradeMap = new ConcurrentHashMap<>();
    public Trade saveTrade(Trade trade){
        tradeMap.put(trade.getTradeId(), trade);
        return trade;
    }
    public Optional<Trade> getTradeById(String tradeId){
        return Optional.ofNullable(tradeMap.get(tradeId));
    }
    public List<Trade> getTradesBySymbol(String stockSymbol){
        return tradeMap.values().stream().filter(trade -> trade.getStockSymbol().equalsIgnoreCase(stockSymbol)).toList();
    }
}
