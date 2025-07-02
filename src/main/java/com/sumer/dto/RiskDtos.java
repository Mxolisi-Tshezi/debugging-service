package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiskDtos {

    @JsonProperty("deviceMake")
    private String deviceMake;

    @JsonProperty("deviceModel")
    private String deviceModel;

    @JsonProperty("isDevicePrepaid")
    private Boolean isDevicePrepaid;

    @JsonProperty("serviceProvider")
    private String serviceProvider;

    @JsonProperty("addressLine1")
    private String addressLine1;

    @JsonProperty("sumInsured")
    private Double sumInsured;

    @JsonProperty("decline")
    private Boolean decline;
    @JsonProperty("levelOfCover")
    private String levelOfCover;
}
