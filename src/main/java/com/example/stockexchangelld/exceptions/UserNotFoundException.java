package com.example.stockexchangelld.exceptions;

public class UserNotFoundException extends TradingException{
    public UserNotFoundException(String userId) {
        super("User not found:"+ userId);
    }
}
