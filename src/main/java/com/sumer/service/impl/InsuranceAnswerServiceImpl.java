package com.sumer.service.impl;

import com.sumer.config.LoggedInUserClient;
import com.sumer.dto.InsuranceAnswerRequest;
import com.sumer.dto.QuestionAnswerDto;
import com.sumer.dto.QuoteStatusResponse;
import com.sumer.dto.UserDetailsDto;
import com.sumer.entity.InsuranceAnswer;
import com.sumer.entity.InsuranceConsent;
import com.sumer.entity.InsuranceConsentStatus;
import com.sumer.repository.InsuranceAnswerRepository;
import com.sumer.repository.InsuranceConsentRepository;
import com.sumer.repository.InsuranceQuestionRepository;
import com.sumer.service.interf.InsuranceAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceAnswerServiceImpl implements InsuranceAnswerService {

    private final InsuranceConsentRepository consentRepository;
    private final InsuranceAnswerRepository answerRepository;
    private final InsuranceQuestionRepository questionRepository;
    private final LoggedInUserClient loggedInUserClient;

    @Override
    @Transactional
    public QuoteStatusResponse processAnswers(InsuranceAnswerRequest request) {
        UserDetailsDto user = loggedInUserClient.getLoggedInUser();
        if (user == null) {
            throw new IllegalStateException("User not authenticated or not found.");
        }

        Long userId = user.getId();
        log.info("Processing answers for user: {}", userId);
        InsuranceConsent consent = consentRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Consent not found for user. Please create consent first."));

        log.info("Found consent for user: {} with status: {}", userId, consent.getConsentStatus());
        if (consent.getConsentStatus() == InsuranceConsentStatus.REJECT) {
            throw new IllegalStateException("Cannot submit answers - your consent has been rejected");
        }
        List<Long> questionIds = request.getAnswers().stream()
                .map(QuestionAnswerDto::getQuestionId)
                .collect(Collectors.toList());

        if (questionRepository.countByIdIn(questionIds) != questionIds.size()) {
            throw new IllegalArgumentException("One or more question IDs are invalid");
        }
        for (Long questionId : questionIds) {
            answerRepository.deleteByUserIdAndQuestionId(userId, questionId);
        }
        List<InsuranceAnswer> answers = request.getAnswers().stream()
                .map(a -> InsuranceAnswer.builder()
                        .userId(userId)
                        .questionId(a.getQuestionId())
                        .answer(a.getAnswer())
                        .build())
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
        log.info("Saved {} answers for user: {}", answers.size(), userId);
        boolean isRisky = answers.stream()
                .anyMatch(a -> a.getAnswer().toLowerCase().contains("declined") ||
                        a.getAnswer().toLowerCase().contains("yes") &&
                                a.getAnswer().toLowerCase().contains("medical"));
        InsuranceConsentStatus newStatus = isRisky ?
                InsuranceConsentStatus.REJECT : InsuranceConsentStatus.ACCEPT;
        consent.setConsentStatus(newStatus);
        consentRepository.save(consent);

        String message = newStatus == InsuranceConsentStatus.ACCEPT ?
                "Congratulations! Your insurance quote is approved." :
                "Your quote has been rejected due to high-risk answers.";

        log.info("Updated consent status for user: {} to: {}", userId, newStatus);

        return QuoteStatusResponse.builder()
                .userId(userId)
                .insuranceConsentStatus(newStatus)
                .message(message)
                .build();
    }
}
