package com.sumer.controller;

import com.sumer.dto.*;
import com.sumer.entity.Insurance;
import com.sumer.service.impl.AuthenticationService;
import com.sumer.service.impl.EmailService;
import com.sumer.service.interf.InsuranceService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/insurance")
@RequiredArgsConstructor
@Slf4j
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping(path = "/token")
    public ResponseEntity<TokenResponseDto> getAccessToken() {
        TokenResponseDto token = authenticationService.getAccessToken();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/create/policy")
    public ResponseEntity<Response> createInsurancePolicy(@RequestBody InsuranceDTO insuranceDTO) {
        Response response = insuranceService.createInsurancePolicy(insuranceDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createInsurance(@RequestBody InsuranceDTO insuranceDTO) {
        Response response = insuranceService.createInsurance(insuranceDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update/{policyId}")
    public ResponseEntity<Response> updateInsurance(@PathVariable Long policyId, @RequestBody @Valid InsuranceDTO insuranceDTO) {
        Response response = insuranceService.updateInsurance(policyId, insuranceDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/quote/{insuranceId}")
    public ResponseEntity<Response> getQuote(@PathVariable Long insuranceId) {
        Response response = insuranceService.getQuote(insuranceId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<Response> getPolicy(@PathVariable String policyId) {
        Response response = insuranceService.getPolicy(policyId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/policy/{policyId}")
    public ResponseEntity<Response> updateInsurancePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody InsuranceDTO insuranceDTO) {
        Response response = insuranceService.updateInsurancePolicy(policyId, insuranceDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/policy/{policyId}")
    public ResponseEntity<Response> partiallyUpdateInsurancePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody Insurance insurance) {
        Response response = insuranceService.partiallyUpdateInsurancePolicy(policyId, insurance);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

  /*  @PostMapping("/consent")
    public ResponseEntity<Response> submitConsent(@Valid @RequestBody ConsentRequestDto consentRequestDto) {
        Response response = insuranceService.submitConsent(consentRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }*/

    @GetMapping("/{id}/quote")
    public Response getQuotePrice(@PathVariable Long id) {
        return insuranceService.getQuotePrice(id);
    }

}