package com.sumer.controller;

import com.sumer.dto.InsuranceAnswerRequest;
import com.sumer.dto.QuoteStatusResponse;
import com.sumer.service.interf.InsuranceAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance/answers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InsuranceAnswerController {

    private final InsuranceAnswerService insuranceAnswerService;

    @PostMapping("/submit")
    public ResponseEntity<QuoteStatusResponse> submitAnswers(@RequestBody InsuranceAnswerRequest request) {
        try {
            log.info("Submitting {} answers",
                    request.getAnswers() != null ? request.getAnswers().size() : 0);
            QuoteStatusResponse response = insuranceAnswerService.processAnswers(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error processing answers: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    QuoteStatusResponse.builder()
                            .insuranceConsentStatus(null)
                            .message(e.getMessage())
                            .build()
            );
        } catch (Exception ex) {
            log.error("Unexpected error processing answers", ex);
            return ResponseEntity.internalServerError().body(
                    QuoteStatusResponse.builder()
                            .insuranceConsentStatus(null)
                            .message("An unexpected error occurred while processing your answers.")
                            .build()
            );
        }
    }
}
