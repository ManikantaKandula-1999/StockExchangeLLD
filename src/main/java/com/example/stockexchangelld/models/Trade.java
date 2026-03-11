package com.example.stockexchangelld.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Trade {

    @Builder.Default
    private String TradeId = UUID.randomUUID().toString();

    @NotBlank(message = "Buyer Order is required")
    private String buyerOrderId;

    @NotBlank(message = "Seller Order is required")
    private String sellerOrderId;

    @NotBlank(message = "Stock Symbol is required")
    private String stockSymbol;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;
}
