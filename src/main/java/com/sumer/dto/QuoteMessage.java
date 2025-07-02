package com.sumer.dto;

import lombok.Data;

@Data
public class QuoteMessage {
    private InsuranceDTO insuranceDTO;
    private String action; // e.g., "CREATE_QUOTE", "GET_POLICY"
}
