package com.sumer.dto;

import com.sumer.entity.InsuranceConsentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// InsuranceConsentOnlyRequest.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceConsentOnlyRequest {
    @NotNull
    private Long userId;
    @NotNull
    private QuoteStatus consentStatus;
}



