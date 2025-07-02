package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PolicyHeaderDTO {
    private Long id;
    private int productId;
    private int subCampaignId;
    private int commissionVersion;
    private String oldPolicyNumber;
    private String actualPolicyNumber;
    private String comment;
    private String countryAlpha2Code;
    private String coverStartDate;
}
