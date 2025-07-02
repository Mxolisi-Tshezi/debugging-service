package com.sumer.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteStatusResponseDto {

    private Long userId;
    private String quoteStatus; // APPROVED, REJECTED, PENDING_REVIEW
    private String insuranceType;
    private double estimatedPremium;
    private double estimatedCommission;
    private double coverAmount;
    private String reasonCode;
    private String reasonMessage;
    private List<String> conditions; // Any conditions for approval
    private List<String> exclusions; // Any exclusions
    private LocalDateTime processedAt;
    private String referenceNumber;
    private LocalDateTime validUntil;
    private String nextSteps; // What the customer should do next
    private ContactInfoDto contactInfo; // Contact information for follow-up
}
