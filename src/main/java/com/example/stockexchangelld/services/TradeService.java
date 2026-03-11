package com.example.stockexchangelld.services;

import com.example.stockexchangelld.models.Trade;

import java.util.List;
import java.util.Optional;

public interface TradeService {
    Trade saveTrade(Trade trade);
    Optional<Trade> getTradeById(String tradeId);
    List<Trade> getTradesBySymbol(String stockSymbol);
}
