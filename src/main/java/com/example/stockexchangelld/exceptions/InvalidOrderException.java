package com.example.stockexchangelld.exceptions;

public class InvalidOrderException extends TradingException{
    public InvalidOrderException(String message) {
        super("Invalid order:"+message);
    }
}
