package com.example.stockexchangelld.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Stock {
    @Builder.Default
    private String stockId = UUID.randomUUID().toString();

    @NotBlank(message = "Stock name is required")
    private String stockName;

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotNull(message = "Stock price is required")
    private double stockPrice;
}
