/*
package com.sumer.controller;


import com.sumer.dto.OzowPaymentDto;
import com.sumer.service.impl.OzowPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ozow")
public class OzowPaymentController {

    private final OzowPaymentService ozowPaymentService;

    @Autowired
    public OzowPaymentController(OzowPaymentService ozowPaymentService) {
        this.ozowPaymentService = ozowPaymentService;
    }

    @PostMapping("/initiate-payment")
    public ResponseEntity<String> initiatePayment(@RequestBody OzowPaymentDto paymentDto) {
        try {
            // Call the initiateOzowPayment method of the service
            String response = ozowPaymentService.initiateOzowPayment(paymentDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Return a custom error message if something goes wrong
            return new ResponseEntity<>("Error initiating Ozow payment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

*/
