/*
package com.sumer.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import com.sumer.service.impl.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PayPalPaymentController {
    private final PaypalService paypalService;
    @PostMapping("/payment")
    public ResponseEntity<?> createPayment(
            @RequestParam("total") Double total,
            @RequestParam("currency") String currency,
            @RequestParam("method") String method,
            @RequestParam("intent") String intent,
            @RequestParam("description") String description,
            @RequestParam("cancelUrl") String cancelUrl,
            @RequestParam("successUrl") String successUrl
    ) {
        try {
            Payment payment = paypalService.createPayment(
                    total, currency, method, intent, description, cancelUrl, successUrl
            );

            for (var link : payment.getLinks()) {
                if ("approval_url".equalsIgnoreCase(link.getRel())) {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "SUCCESS");
                    response.put("approval_url", link.getHref());
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.badRequest().body("No approval URL found in the PayPal response.");

        } catch (PayPalRESTException e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/execute")
    public ResponseEntity<?> executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            return ResponseEntity.ok(payment);
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(500).body("Error executing PayPal payment: " + e.getMessage());
        }
    }
}
*/
