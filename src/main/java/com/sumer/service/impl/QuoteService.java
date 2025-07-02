package com.sumer.service.impl;

import com.sumer.dto.CreateQuoteRequestDto;
import com.sumer.dto.CustomerDto;
import com.sumer.entity.Quote;
import com.sumer.entity.Risk;
import com.sumer.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public CreateQuoteRequestDto createAndSaveQuote(CustomerDto customerDto) {
        if (customerDto == null) {
            throw new IllegalArgumentException("Customer data cannot be null");
        }

        // Map customer DTO to CreateQuoteRequestDto
        CreateQuoteRequestDto quoteRequest = mapCustomerToQuoteRequest(customerDto);

        // Map quote request DTO to Quote entity and set additional fields
        Quote quote = new Quote();
        quote.setCompanyName(quoteRequest.getCompanyName());
        quote.setPostalCode(quoteRequest.getPostalCode());
        quote.setSuburb(quoteRequest.getSuburb());
        quote.setAddressLine1(quoteRequest.getAddressLine1());
        quote.setContactName(quoteRequest.getContactName());
        quote.setSumInsured(quoteRequest.getSumInsured());
        quote.setIsDevicePrepaid(quoteRequest.getIsDevicePrepaid());
        quote.setPolicyType(quoteRequest.getPolicyType());
        quote.setVatRegistered(quoteRequest.getVatRegistered());

        // Set default service provider to Vodacom if none provided
        if (quoteRequest.getServiceProvider() == null || quoteRequest.getServiceProvider().isEmpty()) {
            quote.setServiceProvider("Vodacom");
        } else {
            quote.setServiceProvider(quoteRequest.getServiceProvider());
        }

        // Map risk DTOs to Risk entities
        if (quoteRequest.getRisks() != null && !quoteRequest.getRisks().isEmpty()) {
            List<Risk> risks = quoteRequest.getRisks().stream().map(riskDto -> {
                Risk risk = new Risk();
                risk.setDeviceMake(riskDto.getDeviceMake());
                risk.setDeviceModel(riskDto.getDeviceModel());
                //risk.setIsDevicePrepaid(riskDto.getIsDevicePrepaid());
                risk.setServiceProvider(riskDto.getServiceProvider());
                risk.setAddressLine1(riskDto.getAddressLine1());
                risk.setSumInsured(riskDto.getSumInsured());
                risk.setDecline(riskDto.getDecline());
                return risk;
            }).collect(Collectors.toList());

            quote.setRisks(risks);
        } else {
            // Set empty list if there are no risks
            quote.setRisks(List.of());
        }

        // Save the Quote entity to the database
        quoteRepository.save(quote);

        // Return the DTO
        return quoteRequest;
    }

    private CreateQuoteRequestDto mapCustomerToQuoteRequest(CustomerDto customerDto) {
        CreateQuoteRequestDto quoteRequest = new CreateQuoteRequestDto();
        quoteRequest.setCompanyName(customerDto.getCompanyName());
        quoteRequest.setPostalCode(customerDto.getPostalCode());
        quoteRequest.setSuburb(customerDto.getSuburb());
        quoteRequest.setAddressLine1(customerDto.getAddressLine1());
        quoteRequest.setContactName(customerDto.getContactName());
        quoteRequest.setSumInsured(customerDto.getSumInsured());
        quoteRequest.setIsDevicePrepaid(customerDto.isDevicePrepaid());
        return quoteRequest;
    }
    public boolean deleteQuote(Long quoteId) {
        if (quoteRepository.existsById(quoteId)) {
            quoteRepository.deleteById(quoteId);
            return true;
        } else {
            return false;
        }
    }
}
