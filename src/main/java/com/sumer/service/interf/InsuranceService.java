package com.sumer.service.interf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sumer.dto.*;
import com.sumer.entity.Insurance;
import jakarta.validation.Valid;

public interface InsuranceService{
    Response createInsurance(InsuranceDTO insuranceDTO);

    Response createInsurancePolicy(InsuranceDTO insuranceDTO);

    Response getQuote(Long insuranceId);

    Response getPolicy(String policyId);
    Response updateInsurancePolicy(Long policyId, InsuranceDTO insuranceDTO);
    Response partiallyUpdateInsurancePolicy(Long policyId, Insurance insuranceDTO);

    Response getQuotePrice(Long id);

    Response updateInsurance(Long policyId, InsuranceDTO insuranceDTO);
}
