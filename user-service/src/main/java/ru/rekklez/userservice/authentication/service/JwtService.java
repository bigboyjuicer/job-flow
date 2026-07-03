package ru.rekklez.userservice.authentication.service;

public interface JwtService {
    String generateJwt(String email);
}
