package com.example.stockexchangelld.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Builder.Default
    private String userId = UUID.randomUUID().toString();

    @NotBlank(message ="Username is required")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;
}
