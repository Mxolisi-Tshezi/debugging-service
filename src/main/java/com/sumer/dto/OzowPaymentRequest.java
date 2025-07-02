package com.sumer.dto;

import lombok.Data;

@Data
public class OzowPaymentRequest {
    private String SiteCode;
    private String CountryCode;
    private String CurrencyCode;
    private double Amount;
    private String TransactionReference;
    private String BankReference;
    private String cancelUrl;
    private String errorUrl;
    private String successUrl;
    private String notifyUrl;
    private boolean IsTest;
    private boolean allowVariableAmount;
    private String HashCheck;
}
