package com.sumer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sumer.entity.InsuranceConsent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConsentRequest {
    @JsonProperty("insurance_consent")
    private InsuranceConsent insuranceConsent;
}
