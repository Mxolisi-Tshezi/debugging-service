package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("suburb")
    private String suburb;

    @JsonProperty("addressLine1")
    private String addressLine1;

    @JsonProperty("contactName")
    private String contactName;

    @JsonProperty("sumInsured")
    private double sumInsured;

    @JsonProperty("isDevicePrepaid")
    private boolean isDevicePrepaid;

    @JsonProperty("risks")
    private List<RiskDtos> risks;

    @JsonProperty("policyType")
    private String policyType;

    @JsonProperty("vatRegistered")
    private boolean vatRegistered;
}
