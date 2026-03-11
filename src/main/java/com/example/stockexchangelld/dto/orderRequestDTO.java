package com.example.stockexchangelld.dto;

import com.example.stockexchangelld.models.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class orderRequestDTO {
    @NotBlank(message = "User Id is required")
    private String userId;

    @NotBlank(message = "Order Type is required")
    private OrderType orderType;

    @NotBlank(message = "Stock Symbol is required")
    private String stockSymbol;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;
}
