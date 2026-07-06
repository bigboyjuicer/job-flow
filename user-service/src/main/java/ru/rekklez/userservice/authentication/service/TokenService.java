package ru.rekklez.userservice.authentication.service;

public interface TokenService {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);
    boolean refreshTokenIsValid(String refreshToken);
    void evictRefreshToken(String email);
    String extractEmailFromAccessToken(String token);
    String extractEmailFromRefreshToken(String token);
}
