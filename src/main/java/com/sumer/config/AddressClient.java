package com.sumer.config;

import com.sumer.dto.AddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressClient {
    private final RestTemplate restTemplate;
    private static final String ADDRESS_SERVICE_URL = "https://vxzro3ib15.execute-api.eu-north-1.amazonaws.com/v1/address/{id}";

    public AddressResponse getAddressById(Long addressId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<AddressResponse> response = restTemplate.exchange(
                    ADDRESS_SERVICE_URL,
                    HttpMethod.GET,
                    requestEntity,
                    AddressResponse.class,
                    addressId
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.warn("Address service returned status: {}", response.getStatusCode());
                return AddressResponse.builder()
                        .code(response.getStatusCodeValue())
                        .message("Address service error")
                        .build();
            }

            return response.getBody();

        } catch (Exception e) {
            log.error("Address fetch error for ID: {}", addressId, e);
            return AddressResponse.builder()
                    .code(500)
                    .message("Address service unavailable: " + e.getMessage())
                    .build();
        }
    }
}
