package com.sumer.controller;

import com.sumer.dto.CreateQuoteRequestDto;
import com.sumer.dto.CustomerDto;
import com.sumer.service.impl.QuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateQuoteRequestDto> createQuote(@RequestBody CustomerDto customerDto) {
        try {
            CreateQuoteRequestDto createdQuote = quoteService.createAndSaveQuote(customerDto);
            return ResponseEntity.ok(createdQuote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{quoteId}")
    public ResponseEntity<String> deleteQuote(@PathVariable Long quoteId) {
        try {
            boolean isDeleted = quoteService.deleteQuote(quoteId);
            if (isDeleted) {
                return ResponseEntity.ok("Quote deleted successfully");
            } else {
                return ResponseEntity.badRequest().body("Quote not found or couldn't be deleted");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error occurred while deleting the quote");
        }
    }
}