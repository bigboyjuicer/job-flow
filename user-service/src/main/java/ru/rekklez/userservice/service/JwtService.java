package ru.rekklez.userservice.service;

public interface JwtService {
    String generateJwt(String email);
}
