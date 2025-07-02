package com.sumer.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumer.dto.InsuranceDTO;

import com.sumer.dto.QuoteMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {

    @Value("${cloud.aws.end-point.uri}")
    private String queueUrl;

    private final AmazonSQS amazonSQS;

    public MessageQueueService(AmazonSQS amazonSQS) {
        this.amazonSQS = amazonSQS;
    }

    public void sendMessageToQueue(Long insuranceId, String eventType) {
        QuoteMessage message = new QuoteMessage();
        message.setInsuranceDTO(new InsuranceDTO(insuranceId));
        message.setAction(eventType);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(serializeMessage(message));
        amazonSQS.sendMessage(sendMessageRequest);
    }

    private String serializeMessage(QuoteMessage message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
