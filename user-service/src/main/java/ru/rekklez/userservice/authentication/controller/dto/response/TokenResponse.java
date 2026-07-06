package ru.rekklez.userservice.authentication.controller.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
