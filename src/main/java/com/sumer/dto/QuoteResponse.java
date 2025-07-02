package com.sumer.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class QuoteResponse {
    private String companyName;
    private String contactName;
    private double sumInsured;
    private List<RiskDtos> risks;
    private String policyType;
    private boolean vatRegistered;
}

