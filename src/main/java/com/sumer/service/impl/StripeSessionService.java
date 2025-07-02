/*
package com.sumer.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.sumer.dto.ProductRequest;
import com.sumer.dto.StripeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeSessionService {

    @Value("${stripe.secret}")
    private String stripeApiKey;

    public StripeResponse checkoutProducts(ProductRequest productRequest) {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getName())
                        .build();
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "USD")
                        .setUnitAmount(productRequest.getAmount())
                        .setProductData(productData)
                        .build();
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(productRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();
        SessionCreateParams.Builder sessionBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:80/success")
                .setCancelUrl("http://localhost:80/cancel")
                .addLineItem(lineItem)
                .setCustomerEmail(productRequest.getUserEmail())
                .putMetadata("userName", productRequest.getUserName() != null ? productRequest.getUserName() : "")
                .putMetadata("userId", productRequest.getUserId() != null ? productRequest.getUserId() : "");
        try {
            Session session = Session.create(sessionBuilder.build());
            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();
        } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Stripe error: " + e.getMessage())
                    .build();
        }
    }
}
*/
