package ru.rekklez.userservice.authentication.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(

        @NotBlank(message = "Refresh токен не должен быть пустым или null")
        String refreshToken
) {
}
