package com.sumer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateQuoteRequestDto {
    private String companyName;
    private String postalCode;
    private String suburb;
    private String addressLine1;
    private String contactName;
    private Double sumInsured;
    private Boolean isDevicePrepaid;
    private String policyType;
    private Boolean vatRegistered;
    private String serviceProvider;

    // Initialize the risks list to avoid null pointer exceptions
    private List<RiskDtos> risks = new ArrayList<>();
}

