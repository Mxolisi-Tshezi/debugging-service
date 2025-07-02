package com.sumer.dto;

import com.sumer.entity.InsuranceConsentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuoteStatusResponse {
    private Long userId;
    private InsuranceConsentStatus insuranceConsentStatus;
    private String message;
}

