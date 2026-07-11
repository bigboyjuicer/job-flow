package ru.rekklez.vacancyservice.authentication.service;

import io.jsonwebtoken.Claims;

public interface TokenService {
    Claims extractClaims(String token);
}
