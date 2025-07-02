package com.sumer.service.impl;

import com.amazonaws.services.sns.model.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumer.config.UserClient;
import com.sumer.dto.*;
import com.sumer.entity.*;
import com.sumer.mapper.EntityDtoMapper;
import com.sumer.repository.*;
import com.sumer.service.interf.InsuranceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsuranceImpl implements InsuranceService {

    @Value("${spring.insurance.url}")
    private String insuranceUrl;

    @Value("${spring.policy.url}")
    private String policyUrl;

    private final InsuranceRepository insuranceRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final AuthenticationService authenticationService;
    private final RestTemplate restTemplate;
    private final AddressRepository addressRepository;
    private final UserConsentRepository userConsentRepository;
    //private final QuoteApplicationRepository quoteApplicationRepository;
    private final InsuranceQuestionRepository insuranceQuestionRepository;
    // final QuoteAnswerRepository quoteAnswerRepository;
    //private final QuoteConditionRepository quoteConditionRepository;
    private final UserClient userClient;

    @Autowired
    private MessageQueueService messageQueueService;
    @Override
    public Response createInsurance(@Valid InsuranceDTO insuranceDTO) {
        try {
            Insurance insurance = entityDtoMapper.mapToInsuranceEntity(insuranceDTO);
            insuranceRepository.save(insurance);
            TokenResponseDto tokenResponse = authenticationService.getAccessToken();
            String token = tokenResponse.getAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<InsuranceDTO> request = new HttpEntity<>(insuranceDTO, headers);
            ResponseEntity<String> response = restTemplate.exchange(insuranceUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseBody = objectMapper.readTree(response.getBody());

                // Extract premium and commission from response
                double premium = responseBody.path("content").path("policy").path("finance").path("termValues").path("premium").asDouble();
                double commission = responseBody.path("content").path("policy").path("finance").path("termValues").path("commission").asDouble();
                double quotePrice = premium + commission; // Calculate quote price

                insurance.setPremium(premium);
                insurance.setCommission(commission);
                insurance.setQuotePrice(quotePrice); // Save quote price to entity
                insuranceRepository.save(insurance);

                // Create response with all three values
                QuoteResponseDto quoteResponse = new QuoteResponseDto();
                quoteResponse.setPremium(premium);
                quoteResponse.setCommission(commission);
                quoteResponse.setQuotePrice(quotePrice); // Include quote price in DTO

                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Insurance successfully created")
                        .quotePrice(quotePrice) // Now includes quotePrice
                        .build();
            } else {
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to create insurance: " + response.getBody())
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error creating insurance: " + e.getMessage())
                    .build();
        }
    }
    @Override
    public Response getQuotePrice(Long id) {
        try {
            Optional<Insurance> insuranceOptional = insuranceRepository.findById(id);

            if (insuranceOptional.isPresent()) {
                Insurance insurance = insuranceOptional.get();

                Double premium = Optional.ofNullable(insurance.getPremium()).orElse(0.0);
                Double commission = Optional.ofNullable(insurance.getCommission()).orElse(0.0);
                Double calculatedQuotePrice = premium + commission;

                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Quote price calculated successfully")
                        .quotePrice(calculatedQuotePrice)
                        .build();
            } else {
                return Response.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Insurance quote not found for id: " + id)
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving quote: " + e.getMessage())
                    .build();
        }
    }
    @Override
    public Response updateInsurance(Long policyId, @Valid InsuranceDTO insuranceDTO) {
        try {
            // Fetch existing insurance policy
            Insurance existingInsurance = insuranceRepository.findById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Insurance policy not found with id: " + policyId));

            // Update premium and commission
            existingInsurance.setPremium(insuranceDTO.getPremium());
            existingInsurance.setCommission(insuranceDTO.getCommission());
            insuranceRepository.save(existingInsurance);

            TokenResponseDto tokenResponse = authenticationService.getAccessToken();
            String token = tokenResponse.getAccessToken();

            // Prepare headers for the external API call
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            // Create new DTO with fields required for external system (including missing fields)
            InsuranceDTO externalInsuranceDTO = new InsuranceDTO();
            externalInsuranceDTO.setPremium(insuranceDTO.getPremium());
            externalInsuranceDTO.setCommission(insuranceDTO.getCommission());

            // Include the missing fields with empty/default values (this is a workaround)
            externalInsuranceDTO.setRisks(new ArrayList<>()); // Empty list for Risks
            externalInsuranceDTO.setHeader(externalInsuranceDTO.getHeader()); // Empty string for Header
            externalInsuranceDTO.setFinance(""); // Empty string for Finance
            externalInsuranceDTO.setDetailFields(new ArrayList<>()); // Empty list for DetailFields

            // Prepare the request entity
            HttpEntity<InsuranceDTO> request = new HttpEntity<>(externalInsuranceDTO, headers);

            // Send the update request to the external system
            ResponseEntity<String> response = restTemplate.exchange(insuranceUrl + "/" + policyId, HttpMethod.PUT, request, String.class);

            // Check if the external system update was successful
            if (response.getStatusCode().is2xxSuccessful()) {
                // Return success response
                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Insurance successfully updated")
                        .data(externalInsuranceDTO)
                        .build();
            } else {
                // Return failure response
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to update insurance: " + response.getBody())
                        .build();
            }

        } catch (ResourceNotFoundException e) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            // Return internal server error for any other exceptions
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error updating insurance: " + e.getMessage())
                    .build();
        }
    }




    @Override
    public Response createInsurancePolicy(InsuranceDTO insuranceDTO) {
        try {
            Insurance insurance = entityDtoMapper.mapToInsuranceEntity(insuranceDTO);
            insuranceRepository.save(insurance);

            TokenResponseDto tokenResponse = authenticationService.getAccessToken();
            String token = tokenResponse.getAccessToken();

            // Prepare headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            // Create the HTTP request
            HttpEntity<InsuranceDTO> request = new HttpEntity<>(insuranceDTO, headers);
            ResponseEntity<String> response = restTemplate.exchange(insuranceUrl, HttpMethod.POST, request, String.class);

            // Check if the request was successful
            if (response.getStatusCode().is2xxSuccessful()) {
                // Send message to queue upon successful creation
                messageQueueService.sendMessageToQueue(insurance.getId(), "CREATE_POLICY");

                // Return a response with success and insurance data
                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Insurance Policy successfully created")
                        .data(insuranceDTO) // Returning insurance data as part of the response
                        .queueMessage(QueueMessage.builder()
                                .insuranceId(insurance.getId())
                                .action("CREATE_POLICY")
                                .status("Message sent to queue")
                                .build())
                        .build();
            } else {
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to create insurance: " + response.getBody())
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error creating insurance: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getQuote(Long insuranceId) {
        try {
            Optional<Insurance> optionalInsurance = insuranceRepository.findById(insuranceId);

            if (optionalInsurance.isPresent()) {
                Insurance insurance = optionalInsurance.get();
                double termPremium = insurance.getPremium();
                double termCommission = insurance.getCommission();

                // Send message to retrieve a policy
                messageQueueService.sendMessageToQueue(insurance.getId(), "GET_QUOTE");

                // Create a QuoteResponseDto with the values
                QuoteResponseDto quoteResponse = new QuoteResponseDto();
                quoteResponse.setPremium(termPremium);
                quoteResponse.setCommission(termCommission);

                // Build the response with queueMessage included
                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Quote successfully retrieved")
                        .data(quoteResponse)
                        .queueMessage(QueueMessage.builder()
                                .insuranceId(insurance.getId())
                                .action("GET_QUOTE")
                                .status("Message sent to queue")
                                .build())
                        .build();
            } else {
                return Response.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Insurance policy not found")
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error fetching quote: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response getPolicy(String policyId) {
        try {
            TokenResponseDto tokenResponse = authenticationService.getAccessToken();
            String token = tokenResponse.getAccessToken(); // Get the access token from the response
            String url = policyUrl + "/" + policyId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, request, Response.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to retrieve policy")
                        .build();
            }
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving policy: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response updateInsurancePolicy(Long policyId, InsuranceDTO insuranceDTO) {
        try {
            Insurance existingInsurance = insuranceRepository.findById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Insurance Policy not found with id: " + policyId));

            Insurance updatedInsurance = entityDtoMapper.mapToInsuranceEntity(insuranceDTO);
            updatedInsurance.setId(policyId);
            insuranceRepository.save(updatedInsurance);

            // Use the new sendAuthenticatedPutRequest method
            ResponseEntity<String> response = authenticationService.sendAuthenticatedPutRequest(
                    insuranceUrl + "/" + policyId, insuranceDTO);

            if (response.getStatusCode().is2xxSuccessful()) {
                messageQueueService.sendMessageToQueue(policyId, "UPDATE_POLICY");
                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Insurance Policy successfully updated")
                        .data(insuranceDTO)
                        .queueMessage(QueueMessage.builder()
                                .insuranceId(policyId)
                                .action("UPDATE_POLICY")
                                .status("Message sent to queue")
                                .build())
                        .build();
            } else {
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to update insurance: " + response.getBody())
                        .build();
            }
        } catch (ResourceNotFoundException e) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error updating insurance: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public Response partiallyUpdateInsurancePolicy(Long policyId, Insurance insurance) {
        try {
            Insurance existingInsurance = insuranceRepository.findById(policyId)
                    .orElseThrow(() -> new ResourceNotFoundException("Insurance Policy not found with id: " + policyId));

            // Update fields as needed
            if (insurance.getPremium() > 0) {
                existingInsurance.setPremium(insurance.getPremium());
            }
            if (insurance.getCommission() > 0) {
                existingInsurance.setCommission(insurance.getCommission());
            }
            if (insurance.getPolicies() != null) {
                existingInsurance.setPolicies(insurance.getPolicies());
            }
            if (insurance.getHeader() != null) {
                existingInsurance.setHeader(insurance.getHeader());
            }
            if (insurance.getDetailFields() != null) {

                existingInsurance.getDetailFields().clear();
                existingInsurance.getDetailFields().addAll(insurance.getDetailFields()); // Assuming Set is used
            }

            // Initialize the addresses collection if it is null
            if (existingInsurance.getAddresses() == null) {
                existingInsurance.setAddresses(new ArrayList<>());
            }

            // Handle address updates
            if (insurance.getAddresses() != null && !insurance.getAddresses().isEmpty()) {
                for (Address newAddress : insurance.getAddresses()) {
                    if (newAddress.getId() == null || newAddress.getId() <= 0) {
                        // New address, add it to the existing insurance
                        newAddress.setInsurance(existingInsurance);
                        existingInsurance.getAddresses().add(newAddress);
                    } else {
                        // Existing address, update it
                        Address existingAddress = addressRepository.findById(newAddress.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + newAddress.getId()));
                        updateAddress(existingAddress, newAddress);
                    }
                }
            }

            // Save updated insurance
            insuranceRepository.save(existingInsurance);

            // Send the authenticated patch request
            ResponseEntity<String> response = authenticationService.sendAuthenticatedPatchRequest(
                    insuranceUrl + "/" + policyId, insurance);

            if (response.getStatusCode().is2xxSuccessful()) {
                messageQueueService.sendMessageToQueue(policyId, "PARTIALLY_UPDATE_POLICY");
                return Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Insurance Policy successfully partially updated")
                        .data(insurance)
                        .queueMessage(QueueMessage.builder()
                                .insuranceId(policyId)
                                .action("PARTIALLY_UPDATE_POLICY")
                                .status("Message sent to queue")
                                .build())
                        .build();
            } else {
                return Response.builder()
                        .status(response.getStatusCode().value())
                        .message("Failed to partially update insurance: " + response.getBody())
                        .build();
            }
        } catch (ResourceNotFoundException e) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error partially updating insurance: " + e.getMessage())
                    .build();
        }
    }


    private void updateAddress(Address existingAddress, Address newAddress) {
        existingAddress.setName(newAddress.getName());
        existingAddress.setStreet(newAddress.getStreet());
        existingAddress.setCity(newAddress.getCity());
        existingAddress.setProvince(newAddress.getProvince());
        existingAddress.setZipCode(newAddress.getZipCode());
        existingAddress.setPhoneNumber(newAddress.getPhoneNumber());
    }

   /* @Override
    public Response submitConsent(ConsentRequestDto consentRequestDto) {
        try {
            InsuranceConsent insuranceConsent = consentRequestDto.getInsuranceConsent();
            if (!insuranceConsent.equals("ACCEPT") && !insuranceConsent.equals("REJECT") && !insuranceConsent.equals("PENDING")) {
                return Response.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid consent status. Must be ACCEPT, REJECT, or PENDING")
                        .build();
            }

            // Fetch user from UserClient
            UserDetailsDto user = userClient.getUserById(consentRequestDto.getUserId());

            if (user == null || user.getId() == null) {
                return Response.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("User not found for ID: " + consentRequestDto.getUserId())
                        .build();
            }

            Long resolvedUserId = user.getId();

            ConsentResponseDto responseDto = ConsentResponseDto.builder()
                    .userId(resolvedUserId)
                    .insuranceConsent(insuranceConsent)
                    .timestamp(LocalDateTime.now())
                    .build();

            UserConsent consent = new UserConsent();
            consent.setUserId(resolvedUserId);
            consent.setInsuranceConsent(insuranceConsent);
            consent.setTimestamp(LocalDateTime.now());
            userConsentRepository.save(consent);

            messageQueueService.sendMessageToQueue(resolvedUserId, "INSURANCE_CONSENT_" + insuranceConsent);

            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Insurance consent successfully recorded for user: " + user.getFirstName() + " " + user.getLastName())
                    .data(responseDto)
                    .queueMessage(QueueMessage.builder()
                            .insuranceId(resolvedUserId)
                            .action("INSURANCE_CONSENT_" + insuranceConsent)
                            .status("Message sent to queue")
                            .build())
                    .build();

        } catch (Exception e) {
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error recording insurance consent: " + e.getMessage())
                    .build();
        }
    }
*/
   /* @Override
    public Response calculateInsuranceQuotePrice(QuotePriceRequestDto quotePriceRequest) {
        try {
            Long userId = quotePriceRequest.getUserId();
            BigDecimal cartTotal = quotePriceRequest.getCartTotal();

            log.info("Calculating insurance quote price for userId: {} with cart total: {}", userId, cartTotal);

            // Calculate quote based on cart total and qualitative answers
            QuotePriceResponseDto quotePrice = calculateQuotePrice(quotePriceRequest);

            // Send message to queue
            messageQueueService.sendMessageToQueue(userId, "QUOTE_PRICE_CALCULATED");

            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Insurance quote price calculated successfully")
                    .data(quotePrice)
                    .queueMessage(QueueMessage.builder()
                            .insuranceId(userId)
                            .action("QUOTE_PRICE_CALCULATED")
                            .status("Message sent to queue")
                            .build())
                    .build();

        } catch (Exception e) {
            log.error("Error calculating insurance quote price for userId: {}", quotePriceRequest.getUserId(), e);
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error calculating quote price: " + e.getMessage())
                    .build();
        }
    }*/

    /*private QuotePriceResponseDto calculateQuotePrice(QuotePriceRequestDto request) {
        BigDecimal cartTotal = request.getCartTotal();
        BigDecimal basePremium = cartTotal.multiply(new BigDecimal("0.08")); // 8% of cart total
        BigDecimal riskMultiplier = new BigDecimal("1.0");

        // Analyze qualitative answers to adjust premium
        for (QuestionAnswerDto answer : request.getQualitativeAnswers()) {
            switch (answer.getQuestionId().intValue()) {
                case 1: // Service provider
                    if ("Other".equalsIgnoreCase(answer.getAnswer())) {
                        riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.1"));
                    }
                    break;

                case 2: // Regular user
                    if ("Child".equalsIgnoreCase(answer.getAnswer())) {
                        riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.2"));
                    }
                    break;

                case 3: // Risk address - could analyze location risk
                    // For now, just check if it's provided
                    if (answer.getAnswer() == null || answer.getAnswer().trim().isEmpty()) {
                        riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.15"));
                    }
                    break;

                case 4: // Previous policy declined
                    if ("Yes".equalsIgnoreCase(answer.getAnswer())) {
                        riskMultiplier = riskMultiplier.multiply(new BigDecimal("1.5"));
                    }
                    break;
            }
        }

        BigDecimal finalPremium = basePremium.multiply(riskMultiplier);
        BigDecimal commission = finalPremium.multiply(new BigDecimal("0.15"));

        return QuotePriceResponseDto.builder()
                .userId(request.getUserId())
                .cartTotal(cartTotal)
                .basePremium(basePremium)
                .riskMultiplier(riskMultiplier)
                .finalPremium(finalPremium)
                .commission(commission)
                .coverageAmount(cartTotal)
                .coveragePeriod(request.getCoveragePeriod())
                .deviceCategory(request.getDeviceCategory())
                .calculatedAt(LocalDateTime.now())
                .validUntil(LocalDateTime.now().plusDays(30))
                .build();
    }*/

   /* @Override
    public Response processQuoteAnswers(QuoteAnswersRequestDto answersRequest) {
        try {
            String insuranceType = answersRequest.getInsuranceType();
            List<QuestionAnswerDto> answers = answersRequest.getAnswers();

            log.info("Processing quote answers for insuranceType: {} with {} answers",
                    insuranceType, answers.size());

            // Validate answers
            String validationError = validateAnswers(answers);
            if (validationError != null) {
                return Response.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(validationError)
                        .build();
            }

            // Process the answers and determine approval status
            QuoteStatusResponseDto quoteStatus = evaluateQuoteAnswers(answersRequest);

            // Save the answers and status to database
            saveQuoteAnswers(answersRequest, quoteStatus);

            // Send message to queue
            String queueAction = "QUOTE_" + quoteStatus.getQuoteStatus();
            if (answersRequest.getUserId() != null) {
                messageQueueService.sendMessageToQueue(answersRequest.getUserId(), queueAction);
            }

            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Quote answers processed successfully")
                    .data(quoteStatus)
                    .queueMessage(answersRequest.getUserId() != null ?
                            QueueMessage.builder()
                                    .insuranceId(answersRequest.getUserId())
                                    .action(queueAction)
                                    .status("Message sent to queue")
                                    .build() : null)
                    .build();

        } catch (Exception e) {
            log.error("Error processing quote answers for insuranceType: {}",
                    answersRequest.getInsuranceType(), e);
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error processing quote answers: " + e.getMessage())
                    .build();
        }
    }*/

    /*@Override
    public Response getQuoteStatus(String referenceNumber) {
        try {
            Optional<QuoteApplication> quoteApplication = quoteApplicationRepository.findByReferenceNumber(referenceNumber);

            if (!quoteApplication.isPresent()) {
                return Response.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Quote not found with reference number: " + referenceNumber)
                        .build();
            }

            QuoteApplication quote = quoteApplication.get();
            QuoteStatusResponseDto statusDto = mapToQuoteStatusDto(quote);

            return Response.builder()
                    .status(HttpStatus.OK.value())
                    .message("Quote status retrieved successfully")
                    .data(statusDto)
                    .build();

        } catch (Exception e) {
            log.error("Error retrieving quote status for reference: {}", referenceNumber, e);
            return Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Error retrieving quote status: " + e.getMessage())
                    .build();
        }

}*/
// Complete implementation of missing methods in InsuranceImpl class

    private String validateAnswers(List<QuestionAnswerDto> answers) {
        if (answers == null || answers.isEmpty()) {
            return "At least one answer is required";
        }

        // Check for required questions
        List<Long> requiredQuestionIds = Arrays.asList(1L, 2L, 3L, 4L); // IDs of required questions
        List<Long> answeredQuestionIds = answers.stream()
                .map(QuestionAnswerDto::getQuestionId)
                .collect(Collectors.toList());

        for (Long requiredId : requiredQuestionIds) {
            if (!answeredQuestionIds.contains(requiredId)) {
                return "Required question with ID " + requiredId + " is missing";
            }
        }

        for (QuestionAnswerDto answer : answers) {
            if (answer.getQuestionId() == null) {
                return "Question ID is required for all answers";
            }

            if (answer.getAnswer() == null || answer.getAnswer().trim().isEmpty()) {
                return "Answer is required for question ID: " + answer.getQuestionId();
            }


        }

        return null;
    }

   /* private QuoteStatusResponseDto evaluateQuoteAnswers(QuoteAnswersRequestDto answersRequest) {
        String quoteStatus = "APPROVED"; // Default
        String reasonCode = "STANDARD_APPROVAL";
        String reasonMessage = "Application meets all standard criteria";
        Double estimatedPremium = 100.0; // Base premium
        Double estimatedCommission = 0.0;
        List<String> conditions = new ArrayList<>();
        List<String> exclusions = new ArrayList<>();
        String nextSteps = "Your quote has been approved. Please proceed with payment to activate your policy.";

        // Risk scoring system
        int riskScore = 0;

        // Business logic to evaluate answers and determine approval/rejection
        for (QuestionAnswerDto answer : answersRequest.getAnswers()) {

            // Evaluate based on question ID and field name
            if (answer.getQuestionId() != null) {
                switch (answer.getQuestionId().intValue()) {
                    case 1: // Service Provider question
                        if ("Other".equalsIgnoreCase(answer.getAnswer())) {
                            conditions.add("Manual verification of service provider required");
                            estimatedPremium += 25.0;
                            riskScore += 1;
                        } else if ("Cell C".equalsIgnoreCase(answer.getAnswer())) {
                            // Slightly higher risk for smaller networks
                            estimatedPremium += 10.0;
                            riskScore += 1;
                        }
                        break;

                    case 2: // Device User question
                        String deviceUser = answer.getAnswer().trim();
                        if (deviceUser.length() < 2) {
                            quoteStatus = "REJECTED";
                            reasonCode = "INVALID_USER_INFO";
                            reasonMessage = "Invalid device user information provided";
                            riskScore += 10; // High risk for invalid info
                        } else if (deviceUser.toLowerCase().contains("company") ||
                                deviceUser.toLowerCase().contains("business")) {
                            conditions.add("Business use requires additional verification");
                            estimatedPremium += 50.0;
                            riskScore += 2;
                        }
                        break;

                    case 3: // Risk Address question
                        String address = answer.getAnswer().toLowerCase();

                        // High-risk areas (you can expand this list)
                        List<String> highRiskAreas = Arrays.asList(
                                "hillbrow", "alexandra", "soweto", "khayelitsha",
                                "mitchells plain", "manenberg", "nyanga"
                        );

                        boolean isHighRiskArea = highRiskAreas.stream()
                                .anyMatch(area -> address.contains(area));

                        if (isHighRiskArea) {
                            quoteStatus = "PENDING_REVIEW";
                            reasonCode = "HIGH_RISK_LOCATION";
                            reasonMessage = "Manual review required due to high-risk location";
                            conditions.add("High-risk location requires additional security measures");
                            estimatedPremium += 100.0;
                            riskScore += 5;
                            nextSteps = "Your application requires manual review due to location. We will contact you within 2 business days.";
                        } else if (address.contains("rural") || address.contains("farm")) {
                            conditions.add("Rural location may have limited coverage options");
                            estimatedPremium += 30.0;
                            riskScore += 2;
                        }

                        // Check for incomplete address
                        if (!address.contains("street") && !address.contains("road") &&
                                !address.contains("avenue") && !address.contains("drive")) {
                            conditions.add("Complete address verification required");
                            riskScore += 1;
                        }
                        break;

                    case 4: // Previous Insurance Decline question
                        if ("Yes".equalsIgnoreCase(answer.getAnswer())) {
                            quoteStatus = "PENDING_REVIEW";
                            reasonCode = "PREVIOUS_DECLINE_HISTORY";
                            reasonMessage = "Manual review required due to previous insurance decline history";
                            conditions.add("Previous insurance decline history requires verification");
                            conditions.add("Additional documentation may be required");
                            estimatedPremium += 75.0;
                            riskScore += 4;
                            nextSteps = "Your application requires manual review due to previous insurance history. We will contact you within 2 business days.";
                        }
                        break;

                    default:
                        // Handle additional questions if any
                        break;
                }
            }

            // Stop processing if already rejected
            if ("REJECTED".equals(quoteStatus)) {
                break;
            }
        }

        // Final risk assessment
        if (riskScore >= 8 && !"REJECTED".equals(quoteStatus)) {
            quoteStatus = "PENDING_REVIEW";
            reasonCode = "HIGH_RISK_PROFILE";
            reasonMessage = "Manual review required due to high overall risk score";
            nextSteps = "Your application requires manual review. We will contact you within 2 business days.";
        }

        // Calculate final premium and commission based on estimated value
        if ("APPROVED".equals(quoteStatus)) {
            if (answersRequest.getEstimatedValue() != null && answersRequest.getEstimatedValue() > 0) {
                // Premium calculation: base rate + value-based premium + risk adjustments
                double valueBasedPremium = answersRequest.getEstimatedValue() * 0.025; // 2.5% of value
                estimatedPremium += valueBasedPremium;

                // Apply risk multiplier
                if (riskScore > 0) {
                    double riskMultiplier = 1.0 + (riskScore * 0.05); // 5% increase per risk point
                    estimatedPremium *= riskMultiplier;
                }
            }

            estimatedCommission = estimatedPremium * 0.15; // 15% commission

            // Add standard exclusions for approved quotes
            exclusions.addAll(getStandardExclusions());

        } else if ("REJECTED".equals(quoteStatus)) {
            estimatedPremium = 0.0;
            estimatedCommission = 0.0;
            nextSteps = "Unfortunately, we cannot provide coverage at this time. Please contact our support team for more information.";
            exclusions.clear();
            conditions.clear();
        } else if ("PENDING_REVIEW".equals(quoteStatus)) {
            // For pending review, provide estimated premium but mark as provisional
            if (answersRequest.getEstimatedValue() != null && answersRequest.getEstimatedValue() > 0) {
                double valueBasedPremium = answersRequest.getEstimatedValue() * 0.03; // Slightly higher rate for pending
                estimatedPremium += valueBasedPremium;

                if (riskScore > 0) {
                    double riskMultiplier = 1.0 + (riskScore * 0.07); // Higher risk multiplier for pending
                    estimatedPremium *= riskMultiplier;
                }
            }

            estimatedCommission = estimatedPremium * 0.12; // Lower commission for pending review
            conditions.add("Premium is provisional and subject to review");
            exclusions.addAll(getStandardExclusions());
        }

        // Create contact info
        ContactInfoDto contactInfo = ContactInfoDto.builder()
                .email("support@sumerstores.com")
                .phone("+27 11 123 4567")
                .address("123 Business Park, Johannesburg, 2000")
                .contactPerson("Insurance Support Team")
                .department("Underwriting")
                .build();

        return QuoteStatusResponseDto.builder()
                .userId(answersRequest.getUserId())
                .quoteStatus(quoteStatus)
                .insuranceType(answersRequest.getInsuranceType())
                .estimatedPremium(Math.round(estimatedPremium * 100.0) / 100.0) // Round to 2 decimal places
                .estimatedCommission(Math.round(estimatedCommission * 100.0) / 100.0)
                .coverAmount(answersRequest.getEstimatedValue())
                .reasonCode(reasonCode)
                .reasonMessage(reasonMessage)
                .conditions(conditions)
                .exclusions(exclusions)
                .processedAt(LocalDateTime.now())
                .referenceNumber("QT" + System.currentTimeMillis())
                .validUntil(LocalDateTime.now().plusDays(30))
                .nextSteps(nextSteps)
                .contactInfo(contactInfo)
                .build();
    }

    private void saveQuoteAnswers(QuoteAnswersRequestDto answersRequest, QuoteStatusResponseDto quoteStatus) {
        try {
            QuoteApplication quoteApplication = QuoteApplication.builder()
                    .quoteId(quoteStatus.getReferenceNumber())
                    .userId(answersRequest.getUserId())
                    .insuranceType(answersRequest.getInsuranceType())
                    .coverAmount(answersRequest.getEstimatedValue())
                    .basePremium(100.0)
                    .adjustments(quoteStatus.getEstimatedPremium() - 100.0) // Adjustments
                    .totalPremium(quoteStatus.getEstimatedPremium())
                    .commission(quoteStatus.getEstimatedCommission())
                    .currency("ZAR")
                    .quoteStatus(quoteStatus.getQuoteStatus())
                    .reasonCode(quoteStatus.getReasonCode())
                    .reasonMessage(quoteStatus.getReasonMessage())
                    .referenceNumber(quoteStatus.getReferenceNumber())
                    .createdAt(quoteStatus.getProcessedAt())
                    .validUntil(quoteStatus.getValidUntil())
                    .processedAt(quoteStatus.getProcessedAt())
                    .status("ACTIVE")
                    .build();

            quoteApplication = quoteApplicationRepository.save(quoteApplication);
            log.info("Saved QuoteApplication with ID: {} and reference: {}",
                    quoteApplication.getId(), quoteApplication.getReferenceNumber());

            // Save answers
            if (answersRequest.getAnswers() != null && !answersRequest.getAnswers().isEmpty()) {
                for (QuestionAnswerDto answerDto : answersRequest.getAnswers()) {
                    if (answerDto.getQuestionId() != null) {
                        // Try to find the question, but save answer even if question not found
                        Optional<InsuranceQuestion> questionOpt = insuranceQuestionRepository.findById(answerDto.getQuestionId());

                        QuoteAnswer answer = QuoteAnswer.builder()
                                .quoteApplication(quoteApplication)
                                .question(questionOpt.orElse(null))
                                .answer(answerDto.getAnswer())
                                .build();

                        quoteAnswerRepository.save(answer);
                        log.debug("Saved answer for question ID: {}", answerDto.getQuestionId());
                    }
                }
            }

            // Save conditions
            if (quoteStatus.getConditions() != null && !quoteStatus.getConditions().isEmpty()) {
                for (String conditionText : quoteStatus.getConditions()) {
                    QuoteCondition condition = QuoteCondition.builder()
                            .quoteApplication(quoteApplication)
                            .conditionText(conditionText)
                            .conditionType("REQUIREMENT")
                            .build();

                    quoteConditionRepository.save(condition);
                    log.debug("Saved condition: {}", conditionText);
                }
            }

            // Save exclusions as conditions with different type
            if (quoteStatus.getExclusions() != null && !quoteStatus.getExclusions().isEmpty()) {
                for (String exclusionText : quoteStatus.getExclusions()) {
                    QuoteCondition exclusion = QuoteCondition.builder()
                            .quoteApplication(quoteApplication)
                            .conditionText(exclusionText)
                            .conditionType("EXCLUSION")
                            .build();

                    quoteConditionRepository.save(exclusion);
                    log.debug("Saved exclusion: {}", exclusionText);
                }
            }

            log.info("Successfully saved all quote data for reference: {}", quoteStatus.getReferenceNumber());

        } catch (Exception e) {
            log.error("Error saving quote answers for reference: {}", quoteStatus.getReferenceNumber(), e);
            throw new RuntimeException("Failed to save quote answers: " + e.getMessage(), e);
        }
    }

    private QuoteStatusResponseDto mapToQuoteStatusDto(QuoteApplication quote) {
        try {
            // Fetch conditions and exclusions
            List<QuoteCondition> allConditions = quote.getConditions();

            List<String> conditions = new ArrayList<>();
            List<String> exclusions = new ArrayList<>();

            if (allConditions != null) {
                for (QuoteCondition condition : allConditions) {
                    if ("REQUIREMENT".equals(condition.getConditionType())) {
                        conditions.add(condition.getConditionText());
                    } else if ("EXCLUSION".equals(condition.getConditionType())) {
                        exclusions.add(condition.getConditionText());
                    }
                }
            }

            // If no exclusions saved, add standard ones for approved quotes
            if (exclusions.isEmpty() && "APPROVED".equals(quote.getQuoteStatus())) {
                exclusions.addAll(getStandardExclusions());
            }

            // Create contact info
            ContactInfoDto contactInfo = ContactInfoDto.builder()
                    .email("support@sumerstores.com")
                    .phone("+27 11 123 4567")
                    .address("123 Business Park, Johannesburg, 2000")
                    .contactPerson("Insurance Support Team")
                    .department("Underwriting")
                    .build();

            String nextSteps = determineNextSteps(quote.getQuoteStatus());

            return QuoteStatusResponseDto.builder()
                    .userId(quote.getUserId())
                    .quoteStatus(quote.getQuoteStatus())
                    .insuranceType(quote.getInsuranceType())
                    .estimatedPremium(quote.getTotalPremium())
                    .estimatedCommission(quote.getCommission())
                    .coverAmount(quote.getCoverAmount())
                    .reasonCode(quote.getReasonCode())
                    .reasonMessage(quote.getReasonMessage())
                    .conditions(conditions)
                    .exclusions(exclusions)
                    .processedAt(quote.getProcessedAt())
                    .referenceNumber(quote.getReferenceNumber())
                    .validUntil(quote.getValidUntil())
                    .nextSteps(nextSteps)
                    .contactInfo(contactInfo)
                    .build();

        } catch (Exception e) {
            log.error("Error mapping QuoteApplication to DTO for reference: {}", quote.getReferenceNumber(), e);
            throw new RuntimeException("Failed to map quote data: " + e.getMessage(), e);
        }
    }

    private String determineNextSteps(String quoteStatus) {
        if (quoteStatus == null) {
            return "Please contact our support team for more information about your quote status.";
        }

        switch (quoteStatus.toUpperCase()) {
            case "APPROVED":
                return "Your quote has been approved. Please proceed with payment to activate your policy. " +
                        "You can make payment online or contact our sales team for assistance.";
            case "REJECTED":
                return "Unfortunately, we cannot provide coverage at this time. Please contact our support team " +
                        "at support@sumerstores.com or +27 11 123 4567 for more information or to discuss alternative options.";
            case "PENDING_REVIEW":
                return "Your application requires manual review. Our underwriting team will contact you within 2 business days. " +
                        "Please ensure your contact details are up to date.";
            default:
                return "Please contact our support team for more information about your quote status.";
        }
    }

    private List<String> getStandardExclusions() {
        return Arrays.asList(
                "Damage due to negligence, misuse, or intentional damage",
                "Pre-existing damage not disclosed at time of application",
                "Loss due to war, terrorism, or civil unrest",
                "Damage due to natural disasters (unless specifically covered)",
                "Loss due to theft without proper security measures in place",
                "Damage occurring outside of South Africa (unless travel cover purchased)",
                "Loss due to confiscation by authorities",
                "Damage due to nuclear risks or radioactive contamination",
                "Wear and tear or gradual deterioration",
                "Loss due to fraud or dishonest acts by the insured"
        );
    }*/
}