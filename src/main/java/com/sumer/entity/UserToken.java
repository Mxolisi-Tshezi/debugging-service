package com.sumer.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserToken {
    private Long userId;
    private String token;
    private String role;
    private String insuranceConsent;
    private List<Address> addresses;
}