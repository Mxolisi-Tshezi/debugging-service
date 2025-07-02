/*
package com.sumer.service.impl;


import com.sumer.dto.OzowPaymentRequest;
import com.sumer.dto.OzowPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Slf4j
@RequiredArgsConstructor
public class OzowPaymentService {

    private final RestTemplate restTemplate;

    @Value("${ozow.api.key}")
    private String apiKey;

    @Value("${ozow.private.key}")
    private String privateKey;

    @Value("${ozow.api.url}")
    private String apiUrl;

    public OzowPaymentResponse initiatePayment(OzowPaymentRequest request) {
        try {
            // Validate amount precision
            BigDecimal amount = BigDecimal.valueOf(request.getAmount()).setScale(2, RoundingMode.HALF_EVEN);
            request.setAmount(amount.doubleValue());

            request.setHashCheck(generateHash(request));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.set("ApiKey", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<OzowPaymentRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<OzowPaymentResponse> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    entity,
                    OzowPaymentResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody() != null ? response.getBody() : new OzowPaymentResponse("Empty response body");
            } else {
                String errorDetails = response.getBody() != null ?
                        response.getBody().toString() :
                        "Status code: " + response.getStatusCode();
                log.error("Ozow API Error: {}", errorDetails);
                return new OzowPaymentResponse("Payment failed: " + errorDetails);
            }
        } catch (Exception e) {
            log.error("Ozow payment failed: {}", e.getMessage());
            return new OzowPaymentResponse("Payment processing error: " + e.getMessage());
        }
    }
    private String generateHash(OzowPaymentRequest request) {
        String rawData = String.format("%s%s%s%.2f%s%s%s%s%s%s%s%s",
                request.getSiteCode(),
                request.getCountryCode(),
                request.getCurrencyCode(),
                request.getAmount(),
                request.getTransactionReference(),
                request.getBankReference(),
                request.getCancelUrl(),
                request.getErrorUrl(),
                request.getSuccessUrl(),
                request.getNotifyUrl(),
                request.isIsTest(),
                privateKey
        ).toLowerCase();

        return DigestUtils.sha512Hex(rawData);
    }
}*/
