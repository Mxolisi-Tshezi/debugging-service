package com.sumer.config;

import com.sumer.dto.UserDetailsDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoggedInUserClient {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    private static final String PROFILE_URL = "https://vxzro3ib15.execute-api.eu-north-1.amazonaws.com/v1/user/logged-user";

    public UserDetailsDto getLoggedInUser() {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("No valid Authorization header found");
                return null;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDetailsDto> response = restTemplate.exchange(
                    PROFILE_URL,
                    HttpMethod.GET,
                    entity,
                    UserDetailsDto.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully retrieved user: {}", response.getBody().getId());
                return response.getBody();
            } else {
                log.warn("Failed to retrieve user, status: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            log.error("Error retrieving logged in user", e);
            return null;
        }
    }
}
