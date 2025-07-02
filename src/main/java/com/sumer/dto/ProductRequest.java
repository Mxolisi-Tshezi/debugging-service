package com.sumer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;
    private String userName;
    private String userId;
}