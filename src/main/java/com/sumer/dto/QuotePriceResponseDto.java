package com.sumer.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotePriceResponseDto {

    private Long userId;
    private BigDecimal cartTotal;
    private BigDecimal basePremium;
    private BigDecimal riskMultiplier;
    private BigDecimal finalPremium;
    private BigDecimal commission;
    private BigDecimal coverageAmount;
    private String coveragePeriod;
    private String deviceCategory;
    private LocalDateTime calculatedAt;
    private LocalDateTime validUntil;
    private String quoteReference;
}
