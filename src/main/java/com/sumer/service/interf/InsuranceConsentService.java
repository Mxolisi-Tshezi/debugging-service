package com.sumer.service.interf;

import com.sumer.dto.InsuranceConsentRequest;
import com.sumer.dto.QuoteStatusResponse;

public interface InsuranceConsentService {
    QuoteStatusResponse createConsent();
    QuoteStatusResponse updateConsent(InsuranceConsentRequest request);
    QuoteStatusResponse getConsentStatus();
}
