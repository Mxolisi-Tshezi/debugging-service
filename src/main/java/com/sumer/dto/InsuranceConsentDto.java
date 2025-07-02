package com.sumer.dto;
import com.sumer.entity.InsuranceConsentStatus;
import lombok.Data;
@Data
public class InsuranceConsentDto {
    private Long id;
    private Long userId;
    private InsuranceConsentStatus consentStatus;
}

