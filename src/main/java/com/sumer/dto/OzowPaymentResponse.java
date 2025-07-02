package com.sumer.dto;

import lombok.Data;

@Data
public class OzowPaymentResponse {
    private String paymentRequestId;
    private String url;
    private String errorMessage;
    public OzowPaymentResponse() {}

    public OzowPaymentResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
