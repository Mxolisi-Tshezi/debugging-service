/*
package com.sumer.controller;


import com.sumer.dto.ProductRequest;
import com.sumer.dto.StripeResponse;
import com.sumer.service.impl.StripeSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripeSessionService stripeSessionService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> createCheckoutSession(@Valid @RequestBody ProductRequest productRequest) {
        StripeResponse response = stripeSessionService.checkoutProducts(productRequest);
        return ResponseEntity.ok(response);
    }
}

*/
