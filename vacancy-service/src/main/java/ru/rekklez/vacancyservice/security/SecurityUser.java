package ru.rekklez.vacancyservice.security;

public record SecurityUser(String email,
                           Long id,
                           String role
) {}
