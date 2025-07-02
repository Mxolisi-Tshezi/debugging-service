package com.sumer.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateQuoteDto {
    private String companyName;
    private String postalCode;
    private String suburb;
    private String addressLine1;
    private String contactName;
    private double sumInsured;
    private boolean isDevicePrepaid;
    private List<RiskDtos> risks;
    private String policyType;
    private boolean vatRegistered;
}

