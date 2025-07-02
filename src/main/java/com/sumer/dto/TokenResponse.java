package com.sumer.dto;

public class TokenResponse {
    private String access_token;
    private long expires_in;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public long getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(long expiresIn) {
        this.expires_in = expiresIn;
    }
}
