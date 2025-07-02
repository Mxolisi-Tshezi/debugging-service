package com.sumer.entity;

import com.sumer.config.LoginClient;
import com.sumer.dto.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class LoggedInUserCache {

    private final LoginClient loginClient;

    private final Map<Long, UserToken> tokenCache = new ConcurrentHashMap<>();

    /**
     * Get cached UserToken by user ID
     */
    public UserToken getUserToken(Long userId) {
        return tokenCache.get(userId);
    }

    /**
     * Store a UserToken (alias to putUserToken)
     */
    public void storeUserToken(Long userId, UserToken token) {
        tokenCache.put(userId, token);
    }

    /**
     * Replace the UserToken in the cache
     */
    public void putUserToken(Long userId, UserToken updatedToken) {
        tokenCache.put(userId, updatedToken);
    }

    /**
     * Refresh and update the token in the cache using login credentials
     */
    public UserToken refreshUserToken(String username, String password) {
        UserToken token = loginClient.loginAndGetTokenAndAddress(username, password);
        if (token != null) {
            tokenCache.put(token.getUserId(), token);
        }
        return token;
    }
}
