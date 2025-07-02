package com.sumer.config;

import com.sumer.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;
    private static final String USER_SERVICE_URL = "/{id}";

    public UserDetailsDto getUserById(Long id) {
        try {
            ResponseEntity<UserDetailsDto> response = restTemplate.getForEntity(USER_SERVICE_URL, UserDetailsDto.class, id);
            return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }
}

