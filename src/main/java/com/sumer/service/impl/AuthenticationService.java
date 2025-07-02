package com.sumer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumer.dto.TokenResponseDto;
import com.sumer.entity.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class AuthenticationService {

    @Value("${spring.auth.url}")
    private String authUrl;

    @Value("${spring.auth.client-id}")
    private String clientId;

    @Value("${spring.auth.client-secret}")
    private String clientSecret;

    @Value("${spring.auth.username}")
    private String username;

    @Value("${spring.auth.password}")
    private String password;

    @Value("${spring.auth.scope}")
    private String scope;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthenticationService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public TokenResponseDto getAccessToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);
        body.add("scope", scope);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AuthResponse> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, AuthResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            AuthResponse authResponse = response.getBody();
            TokenResponseDto tokenResponseDto = new TokenResponseDto();
            tokenResponseDto.setAccessToken(Objects.requireNonNull(authResponse).getAccessToken());
            tokenResponseDto.setExpiresIn(authResponse.getExpiresIn());
            tokenResponseDto.setTokenType(authResponse.getTokenType());
            tokenResponseDto.setRefreshToken(authResponse.getRefreshToken());
            tokenResponseDto.setScope(authResponse.getScope());
            return tokenResponseDto;
        } else {
            throw new RuntimeException("Failed to retrieve access token: " + response.getBody());
        }
    }
    public <T> ResponseEntity<String> sendAuthenticatedPutRequest(String url, T requestBody) {
        TokenResponseDto tokenResponse = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenResponse.getAccessToken());
        headers.set("Content-Type", "application/json");

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }
    public <T> ResponseEntity<String> sendAuthenticatedPatchRequest(String url, T requestBody) {
        TokenResponseDto tokenResponse = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenResponse.getAccessToken());
        headers.set("Content-Type", "application/json");

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
    }
}