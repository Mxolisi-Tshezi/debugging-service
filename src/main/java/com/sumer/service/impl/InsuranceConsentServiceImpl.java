package com.sumer.service.impl;

import com.sumer.config.LoggedInUserClient;
import com.sumer.dto.InsuranceConsentRequest;
import com.sumer.dto.InsuranceQuestionAnswerDto;
import com.sumer.dto.QuoteStatusResponse;
import com.sumer.dto.UserDetailsDto;
import com.sumer.entity.InsuranceAnswer;
import com.sumer.entity.InsuranceConsent;
import com.sumer.entity.InsuranceConsentStatus;
import com.sumer.repository.InsuranceAnswerRepository;
import com.sumer.repository.InsuranceConsentRepository;
import com.sumer.service.interf.InsuranceConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceConsentServiceImpl implements InsuranceConsentService {

    private final InsuranceConsentRepository consentRepository;
    private final InsuranceAnswerRepository answerRepository;
    private final LoggedInUserClient loggedInUserClient;

    @Override
    @Transactional
    public QuoteStatusResponse createConsent() {
        UserDetailsDto user = loggedInUserClient.getLoggedInUser();
        if (user == null) {
            throw new IllegalStateException("User not authenticated or not found.");
        }

        Long userId = user.getId();
        log.info("Creating consent for user: {}", userId);

        Optional<InsuranceConsent> existingConsent = consentRepository.findByUserId(userId);
        if (existingConsent.isPresent()) {
            return QuoteStatusResponse.builder()
                    .userId(userId)
                    .insuranceConsentStatus(existingConsent.get().getConsentStatus())
                    .message("Consent already exists for this user")
                    .build();
        }

        // Create new consent with PENDING status
        InsuranceConsent consent = InsuranceConsent.builder()
                .userId(userId)
                .consentStatus(InsuranceConsentStatus.PENDING)
                .build();

        consentRepository.save(consent);
        log.info("Created consent for user: {} with status: {}", userId, consent.getConsentStatus());

        return QuoteStatusResponse.builder()
                .userId(userId)
                .insuranceConsentStatus(InsuranceConsentStatus.PENDING)
                .message("Consent created successfully. Please submit your answers to proceed.")
                .build();
    }

    @Override
    public QuoteStatusResponse getConsentStatus() {
        UserDetailsDto user = loggedInUserClient.getLoggedInUser();
        if (user == null) {
            throw new IllegalStateException("User not authenticated or not found.");
        }

        Long userId = user.getId();
        log.info("Checking consent status for user: {}", userId);

        Optional<InsuranceConsent> consent = consentRepository.findByUserId(userId);

        if (consent.isPresent()) {
            return QuoteStatusResponse.builder()
                    .userId(userId)
                    .insuranceConsentStatus(consent.get().getConsentStatus())
                    .message("Consent found with status: " + consent.get().getConsentStatus())
                    .build();
        } else {
            return QuoteStatusResponse.builder()
                    .userId(userId)
                    .insuranceConsentStatus(null)
                    .message("No consent found for this user")
                    .build();
        }
    }

    @Override
    @Transactional
    public QuoteStatusResponse updateConsent(InsuranceConsentRequest request) {
        UserDetailsDto user = loggedInUserClient.getLoggedInUser();
        if (user == null) {
            throw new IllegalStateException("User not authenticated or not found.");
        }

        Long userId = user.getId();
        log.info("Updating consent for user: {}", userId);

        List<InsuranceQuestionAnswerDto> answers = request.getAnswers();

        // Determine risk based on answers
        boolean risky = answers != null && answers.stream()
                .anyMatch(a -> a.getAnswer() != null &&
                        a.getAnswer().toLowerCase().contains("declined"));

        InsuranceConsentStatus finalStatus = risky ?
                InsuranceConsentStatus.REJECT : InsuranceConsentStatus.ACCEPT;

        // Find or create consent
        Optional<InsuranceConsent> optional = consentRepository.findByUserId(userId);
        InsuranceConsent consent = optional.orElse(
                InsuranceConsent.builder()
                        .userId(userId)
                        .build()
        );

        consent.setConsentStatus(finalStatus);
        consentRepository.save(consent);

        // Save answers if provided
        if (answers != null && !answers.isEmpty()) {
            List<InsuranceAnswer> entities = answers.stream()
                    .map(a -> InsuranceAnswer.builder()
                            .userId(userId)
                            .questionId(a.getQuestionId())
                            .answer(a.getAnswer())
                            .build())
                    .collect(Collectors.toList());
            answerRepository.saveAll(entities);
            log.info("Saved {} answers for user: {}", entities.size(), userId);
        }

        String message = finalStatus == InsuranceConsentStatus.REJECT
                ? "Your quote has been rejected due to high-risk answers."
                : "Congratulations! Your insurance quote is approved.";

        log.info("Updated consent for user: {} with status: {}", userId, finalStatus);

        return QuoteStatusResponse.builder()
                .userId(userId)
                .insuranceConsentStatus(finalStatus)
                .message(message)
                .build();
    }
}
