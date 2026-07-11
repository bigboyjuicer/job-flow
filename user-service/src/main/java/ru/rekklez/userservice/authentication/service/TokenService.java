package ru.rekklez.userservice.authentication.service;

import io.jsonwebtoken.Claims;

public interface TokenService {

    String generateAccessToken(String email, String role, Long id);

    String generateRefreshToken(String email, String role, Long id);

    boolean refreshTokenIsValid(String refreshToken);
    void evictRefreshToken(String email);
    Claims extractClaimsFromAccessToken(String token);
    Claims extractClaimsFromRefreshToken(String token);
}
