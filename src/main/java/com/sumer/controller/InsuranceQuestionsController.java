package com.sumer.controller;

import com.sumer.dto.InsuranceConsentRequest;
import com.sumer.dto.InsuranceQuestionDto;
import com.sumer.dto.QuoteStatusResponse;
import com.sumer.service.interf.InsuranceConsentService;
import com.sumer.service.interf.InsuranceQuestionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance/questions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InsuranceQuestionsController {

    private final InsuranceQuestionsService insuranceQuestionsService;
    private final InsuranceConsentService insuranceConsentService;

    @PostMapping("/create-questions")
    public ResponseEntity<InsuranceQuestionDto> createQuestion(@RequestBody InsuranceQuestionDto questionDto) {
        return ResponseEntity.ok(insuranceQuestionsService.createQuestion(questionDto));
    }

    @GetMapping("/get-question-by-id/{id}")
    public ResponseEntity<InsuranceQuestionDto> getQuestionById(@PathVariable Long id) {
        return insuranceQuestionsService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/get-all-questions")
    public ResponseEntity<List<InsuranceQuestionDto>> getQualitativeQuestions() {
        List<InsuranceQuestionDto> questions = insuranceQuestionsService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/create-consent")
    public ResponseEntity<QuoteStatusResponse> createConsent() {
        try {
            log.info("Creating consent for authenticated user");
            QuoteStatusResponse result = insuranceConsentService.createConsent();
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            log.error("Error creating consent: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    QuoteStatusResponse.builder()
                            .insuranceConsentStatus(null)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
    @GetMapping("/get-consent")
    public ResponseEntity<QuoteStatusResponse> getInsuranceConsent() {
        try {
            log.info("Creating consent for authenticated user via GET");
            QuoteStatusResponse result = insuranceConsentService.getConsentStatus();
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            log.error("Error creating consent: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    QuoteStatusResponse.builder()
                            .insuranceConsentStatus(null)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
    @PostMapping("/submit-answers")
    public ResponseEntity<QuoteStatusResponse> submitConsentWithAnswers(@RequestBody InsuranceConsentRequest request) {
        try {
            log.info("Submitting consent with {} answers",
                    request.getAnswers() != null ? request.getAnswers().size() : 0);
            QuoteStatusResponse result = insuranceConsentService.updateConsent(request);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.error("Error submitting consent: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    QuoteStatusResponse.builder()
                            .insuranceConsentStatus(null)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
