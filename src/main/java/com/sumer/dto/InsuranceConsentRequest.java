package com.sumer.dto;


import com.sumer.entity.InsuranceConsentStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
;

@Data
public class InsuranceConsentRequest {
    private List<InsuranceQuestionAnswerDto> answers;
}



