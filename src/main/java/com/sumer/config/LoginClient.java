package com.sumer.config;

import com.sumer.dto.LoginRequest;
import com.sumer.dto.UserToken;
import com.sumer.entity.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.base-url}")
    private String authServiceBaseUrl;

    public UserToken loginAndGetTokenAndAddress(String username, String password) {
        try {
            String url = authServiceBaseUrl + "/login";

            LoginRequest loginRequest = new LoginRequest(username, password);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<UserToken> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    UserToken.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Login successful for user: {}", username);
                return response.getBody();
            }

        } catch (Exception e) {
            log.error("Login failed for user: {}", username, e);
        }

        return null;
    }

    public List<Address> getAddressesFromLogin(String username, String password) {
        UserToken token = loginAndGetTokenAndAddress(username, password);
        return (token != null) ? token.getAddresses() : List.of();
    }

}
