package com.sumer.dto;

// LoginRequest.

import com.sumer.entity.Address;
import lombok.Builder;
import lombok.Data;

import java.util.List;

// UserToken.java
@Data
@Builder
public class UserToken {
    private Long userId;
    private String token;
    private String role;
    private String insuranceConsent;
    private List<Address> addresses;
}

