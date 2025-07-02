package com.sumer.service.interf;

import com.sumer.dto.InsuranceAnswerRequest;
import com.sumer.dto.QuoteStatusResponse;



public interface InsuranceAnswerService {
    QuoteStatusResponse processAnswers(InsuranceAnswerRequest request);
}

